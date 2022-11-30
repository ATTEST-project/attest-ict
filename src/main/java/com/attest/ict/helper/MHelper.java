package com.attest.ict.helper;

import com.attest.ict.custom.model.matpower.MatpowerModel;
import com.attest.ict.domain.*;
import com.attest.ict.helper.matpower.network.annotated.*;
import com.attest.ict.helper.matpower.network.util.MatpowerAttributesTemplate;
import com.attest.ict.repository.NetworkRepository;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MHelper {

    public static final Logger LOGGER = LoggerFactory.getLogger(MHelper.class);

    static NetworkRepository networkRepository;

    // to call repository in a static function
    @Autowired
    NetworkRepository networkRepository1;

    @PostConstruct
    private void initStaticNetworkRepository() {
        networkRepository = this.networkRepository1;
    }

    // only version '2' of Matpower file is supported
    public static final String MATPOWER_SUPPORTED_VERSION = "2";

    // each section corresponds to a db table
    // these are useful for parsing every matrix of the file
    enum MatpowerSection {
        BUS,
        BRANCH,
        GENERATOR,
        GEN_TAGS,
        BUS_NAMES,
        TRANSFORMER,
        BUS_COORDINATES,
        GEN_COST,
        CAP_BANK_DATA,
        V_LEVELS,
        // LOAD_EL_VAR_P,
        // LOAD_EL_VAR_Q
    }

    private static final List<String> busAttributes = new ArrayList<>();
    private static final List<String> branchAttributes = new ArrayList<>();
    private static final List<String> generatorAttributes = new ArrayList<>();

    private MHelper() {}

    /* isDotMFile
     * check if is file .m
     */
    public static boolean isDotMFile(MultipartFile file) {
        String ext = "";
        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            ext = fileName.substring(index + 1);
        }
        return ext.equals("m");
    }

    /* read method
     + read file passing InputStream, other 'read' method will be called with BufferedReader parameter
     */
    public static MatpowerModel read(InputStream inputStream) throws IOException {
        Objects.requireNonNull(inputStream);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return read(reader);
        }
    }

    /* read method
     * read file passing BufferedReader
     * read file line by line, every matrix is parsed and data is saved in 'MatpowerModel' class
     */
    public static MatpowerModel read(BufferedReader reader) throws IOException {
        Objects.requireNonNull(reader);

        String line = "";

        // String title = processCaseName(line);
        // MatpowerModel model = new MatpowerModel(title);
        MatpowerModel model = null;

        MatpowerSection section = null;
        List<String> lines = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            if (canSkipLine(line)) {
                //skip comments and empty lines
            } else if (line.startsWith("function")) {
                String title = processCaseName(line);
                model = new MatpowerModel(title);
            } else if (line.startsWith("mpc.version ")) {
                processVersion(line, model);
            } else if (line.startsWith("mpc.baseMVA ")) {
                processBaseMva(line, model);
            } else if ((line.startsWith("% bus_i") || line.startsWith("%\tbus_i")) && busAttributes.isEmpty()) {
                parseAttributes(line, "bus");
            } else if (line.startsWith("mpc.bus =") || line.startsWith("mpc.bus=")) {
                section = MatpowerSection.BUS;
            } else if ((line.startsWith("% bus Pg") || line.startsWith("%\tbus\tPg")) && generatorAttributes.isEmpty()) {
                parseAttributes(line, "generator");
            } else if (line.startsWith("mpc.gen =") || line.startsWith("mpc.gen=")) {
                section = MatpowerSection.GENERATOR;
            } else if ((line.startsWith("% fbus") || line.startsWith("%\tfbus")) && branchAttributes.isEmpty()) {
                parseAttributes(line, "branch");
            } else if (line.startsWith("mpc.branch =") || line.startsWith("mpc.branch=")) {
                section = MatpowerSection.BRANCH;
            } else if (line.startsWith("mpc.gen_tags =") || line.startsWith("mpc.gen_tags=")) {
                section = MatpowerSection.GEN_TAGS;
            } else if (line.startsWith("mpc.bus_name =") || line.startsWith("mpc.bus_name=")) {
                section = MatpowerSection.BUS_NAMES;
            } else if ((line.startsWith("mpc.trans =") || line.startsWith("mpc.trans=")) && (line.contains("];") || line.contains("};"))) {
                section = MatpowerSection.TRANSFORMER;
                int firstSB = line.indexOf("[");
                int secondSB = line.indexOf("]");
                lines.add("\t" + line.substring(firstSB + 1, secondSB) + ";");
                section = processEndSection(model, section, lines);
            } else if (line.startsWith("mpc.trans =") || line.startsWith("mpc.trans=")) {
                section = MatpowerSection.TRANSFORMER;
            } else if (line.startsWith("mpc.coordinates =") || line.startsWith("mpc.coordinates=")) {
                section = MatpowerSection.BUS_COORDINATES;
            } else if (line.startsWith("mpc.gencost =") || line.startsWith("mpc.gencost=")) {
                section = MatpowerSection.GEN_COST;
            } else if (line.startsWith("mpc.capacitor_bank_dplan =") || line.startsWith("mpc.capacitor_bank_dplan=")) {
                section = MatpowerSection.CAP_BANK_DATA;
            } else if (line.startsWith("mpc.V_levels =") || line.startsWith("mpc.V_levels=")) {
                section = MatpowerSection.V_LEVELS;
            } /*else if (line.startsWith("mpc.demandP =") || line.startsWith("mpc.demandP=")) {
                section = MatpowerNetworkSection.LOAD_EL_VAR_P;
            } else if (line.startsWith("mpc.demandQ =") || line.startsWith("mpc.demandQ=")) {
                section = MatpowerNetworkSection.LOAD_EL_VAR_Q;
            }*/else if (line.contains("];") || line.contains("};")) {
                section = processEndSection(model, section, lines);
            } else {
                if (section != null) {
                    lines.add(line);
                }
            }
        }

        return model;
    }

    /* processEndSection method
     * data from a specific section is parsed using parseLines method
     */
    private static MatpowerSection processEndSection(MatpowerModel model, MatpowerSection section, List<String> lines) {
        if (section != null) {
            parseLines(lines, model, section);
            lines.clear();
            return null;
        }
        return section;
    }

    /* parseLines method
     * data is parsed and saved in MatpowerModel
     */
    private static void parseLines(List<String> lines, MatpowerModel model, MatpowerSection section) {
        switch (section) {
            case BUS:
                parseBusesLines1(lines, model);
                break;
            case GENERATOR:
                model.getGenerators().addAll(parseLines(lines, GeneratorAnnotated.class));
                break;
            case BRANCH:
                model.getBranches().addAll(parseLines(lines, BranchAnnotated.class));
                break;
            case GEN_TAGS:
                model.getGenTags().addAll(parseLinesForGenTags(lines, GenTagAnnotated.class));
                break;
            case BUS_NAMES:
                model.getBusNames().addAll(parseLinesWithString(lines, BusNameAnnotated.class));
                break;
            case TRANSFORMER:
                model.getTransformers().addAll(parseLines(lines, TransformerAnnotated.class));
                break;
            case BUS_COORDINATES:
                model.getBusCoordinates().addAll(parseLinesForBusCoords(lines, BusCoordinatesAnnotated.class));
                break;
            case GEN_COST:
                model.getGenCosts().addAll(parseLines(lines, GenCostAnnotated.class));
                break;
            case CAP_BANK_DATA:
                model.getCaps().addAll(parseLinesWithString(lines, CapacitorBankDataAnnotated.class));
                break;
            case V_LEVELS:
                model.setvLevels(parseVLevels(lines, VLevelsAnnotated.class));
                break;
            /*case LOAD_EL_VAR_P:
                model.getLoadElVarsP().addAll(parseLines(lines, LoadElVarAnnotated.class));
                break;
            case LOAD_EL_VAR_Q:
                model.getLoadElVarsQ().addAll(parseLines(lines, LoadElVarAnnotated.class));
                break;*/
            default:
                throw new IllegalStateException("Section unknown: " + section);
        }
    }

    private static void parseAttributes(String line, String section) {
        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        TsvParser parser = new TsvParser(settings);
        String[] attributes = parser.parseLine(line);
        List<String> attributesList = Arrays
            .stream(attributes)
            .filter(Objects::nonNull)
            .filter(e -> !e.equals("%"))
            .collect(Collectors.toList());
        switch (section) {
            case "bus":
                busAttributes.addAll(attributesList);
                break;
            case "branch":
                branchAttributes.addAll(attributesList);
                break;
            case "generator":
                generatorAttributes.addAll(attributesList);
                break;
            default:
                LOGGER.error("Section not allowed");
        }
    }

    /* parseVLevels method
     * TSVParser for Voltage Levels section
     */
    private static <T> T parseVLevels(List<String> lines, Class<T> aClass) {
        LOGGER.debug("Parsing data for class {}", aClass);
        BeanListProcessor<T> rowProcessor = new BeanListProcessor<>(aClass);
        TsvParserSettings settings = new TsvParserSettings();
        settings.setProcessor(rowProcessor);
        settings.setHeaderExtractionEnabled(false);
        settings.setLineSeparatorDetectionEnabled(true);
        settings.getFormat().setLineSeparator(";");
        TsvParser parser = new TsvParser(settings);
        lines.stream().map(str -> str.replaceAll("\\s+", "\t").trim()).forEach(parser::parseLine);
        return rowProcessor.getBeans().get(0);
    }

    /* processBaseMva method
     * parse and save BaseMVA value
     */
    private static void processBaseMva(String line, MatpowerModel model) {
        double baseMva = Double.parseDouble(processMatlabAssignment(line));
        model.setBaseMva(baseMva);
    }

    /* processMatlabAssignment method
     * read version line and get value
     */
    private static String processMatlabAssignment(String str) {
        Objects.requireNonNull(str);
        String str2 = str.replace(';', ' ');
        final StringTokenizer st = new StringTokenizer(str2, " ");
        st.nextToken(); // mpc.XYZ
        st.nextToken(); // =
        return st.nextToken();
    }

    /* processVersion method
     * check if version of Matpower file is correct ('2')
     */
    private static void processVersion(String line, MatpowerModel model) {
        String version = processMatlabStringAssignment(line);
        if (!version.equals(MATPOWER_SUPPORTED_VERSION)) {
            throw new IllegalStateException("unsupported MATPOWER version file: " + version);
        }
        model.setVersion(version);
    }

    /* processMatlabStringAssignment method
     * remove single quote from version value
     */
    private static String processMatlabStringAssignment(String str) {
        return processMatlabAssignment(str).replace("'", "");
    }

    /* canSkipLine method
     * if line starts with %, then this is a comment
     * if line length is equal to zero, it is and empty line
     * skip if line is represented by one of these cases
     */
    private static boolean canSkipLine(String line) {
        return line.startsWith("%%") || (line.trim().length() == 0);
    }

    /* processCaseName
     * get model name from 'function mpc'
     */
    private static String processCaseName(String str) {
        /*String str2 = str.replace(';', ' ');
        final StringTokenizer st = new StringTokenizer(str2, " ");
        st.nextToken(); // function
        st.nextToken(); // mpc
        st.nextToken(); // =
        return st.nextToken();*/

        return str.replace(";", " ").substring(str.indexOf("=") + 1).trim();
    }

    /* parseLines method (template)
     * parse lines using TSVParser and annotated classes
     * in case of only simple spaces between values, change them to tab spaces
     * in case of inline comments in rows with data it is necessary to remove them
     * both of these replacements are made with regex (faster)
     * return List of objects
     */
    private static <T> List<T> parseLines(List<String> lines, Class<T> aClass) {
        LOGGER.debug("Parsing data for class {}", aClass);
        BeanListProcessor<T> rowProcessor = new BeanListProcessor<>(aClass);
        TsvParserSettings settings = new TsvParserSettings();
        settings.setProcessor(rowProcessor);
        settings.setHeaderExtractionEnabled(false);
        settings.setLineSeparatorDetectionEnabled(true);
        settings.getFormat().setLineSeparator(";");
        TsvParser parser = new TsvParser(settings);
        lines
            .stream()
            .map(str ->
                str
                    .replaceAll("\\s+", "\t")
                    .replaceAll(";\\t*\\s*%\\s*.*", ";") // rows with values and inline comment
                    .trim()
            )
            .forEach(parser::parseLine);
        return rowProcessor.getBeans();
    }

    private static void parseBusesLines(List<String> lines, MatpowerModel model) {
        LOGGER.debug("Parsing data for buses");
        TsvParserSettings settings1 = new TsvParserSettings();
        settings1.getFormat().setLineSeparator("\n");
        TsvParser parser1 = new TsvParser(settings1);
        String[] firstStringParsed = parser1.parseLine(lines.get(0));
        if (firstStringParsed.length == 13) {
            model.getBuses().addAll(parseLines(lines, BusAnnotated.class));
        } else {
            List<String> busesList = lines
                .stream()
                .map(parser1::parseLine)
                .map(e -> Arrays.asList(e).subList(0, 14))
                .map(e -> String.join("\t", e).replaceAll("null", ""))
                .collect(Collectors.toList());
            model.getBuses().addAll(parseLines(busesList, BusAnnotated.class));

            List<String> busExtensionsList = lines
                .stream()
                .map(parser1::parseLine)
                .map(e -> Arrays.asList(e).subList(14, e.length - 1))
                .map(e -> String.join("\t", e).replaceAll("null", ""))
                .collect(Collectors.toList());
            model.getBusExtensions().addAll(parseLines(busExtensionsList, BusExtensionAnnotated.class));
        }
    }

    private static void parseBusesLines1(List<String> lines, MatpowerModel model) {
        // parse standard buses attributes
        model.getBuses().addAll(parseLines(lines, BusAnnotated.class));

        // parse additional buses attributes (template 1)
        if (busAttributes.equals(MatpowerAttributesTemplate.BUS_EXTENSION_1)) {
            int extensionIndex = busAttributes.indexOf("hasGEN") + 1;

            TsvParserSettings settings = new TsvParserSettings();
            settings.getFormat().setLineSeparator("\n");
            TsvParser parser = new TsvParser(settings);

            // redundant
            /*List<String> busesList = lines.stream()
                    .map(parser::parseLine)
                    .map(e -> Arrays.asList(e).subList(0, extensionIndex))
                    .map(e -> String.join("\t", e).replaceAll("null", ""))
                    .collect(Collectors.toList());
            model.getBuses().addAll(parseLines(busesList, BusAnnotated.class));*/

            List<String> busExtensionsList = lines
                .stream()
                .map(parser::parseLine)
                .map(e -> Arrays.asList(e).subList(extensionIndex, e.length - 1))
                .map(e -> String.join("\t", e).replaceAll("null", ""))
                .collect(Collectors.toList());
            model.getBusExtensions().addAll(parseLines(busExtensionsList, BusExtensionAnnotated.class));
        }
    }

    /* parseLinesForGenTags method (template)
     * in gen tags matrix, remove single quotes from string values
     * split in case of more values in a row
     * then call parseLines 'main' method
     */
    private static <T> List<T> parseLinesForGenTags(List<String> lines, Class<T> aClass) {
        if (lines.size() == 1) {
            String[] arr = lines.get(0).replaceAll("['|\\t]", "").split(";");
            List<String> newLines = Arrays.asList(arr);

            return parseLines(newLines, aClass);
        } else {
            lines = lines.stream().map(str -> str.replaceAll("'", "")).collect(Collectors.toList());

            return parseLines(lines, aClass);
        }
    }

    /* parseLinesWithString method (template)
     * remove single quotes from string values
     * then call parseLines 'main' method
     */
    private static <T> List<T> parseLinesWithString(List<String> lines, Class<T> aClass) {
        lines = lines.stream().map(str -> str.replaceAll("'", "").trim()).collect(Collectors.toList());

        return parseLines(lines, aClass);
    }

    /* parseLinesForBusCoords method (template)
     * remove chars not allowed (... in this case) and split because more values are in the same row
     * then call parseLines 'main' method
     */
    private static <T> List<T> parseLinesForBusCoords(List<String> lines, Class<T> aClass) {
        List<String> newList = new ArrayList<>();
        for (String line : lines) {
            String[] splitLine = line.replaceAll(";\\.\\.\\.", "").split(";");
            newList.addAll(Arrays.asList(splitLine));
        }

        return parseLines(newList, aClass);
    }

    /* createBaseMVATable method
     * return a BaseMVA (made up of value and networkId)
     */
    public static BaseMVA createBaseMVATable(MatpowerModel model, String networkName, Long networkId) {
        BaseMVA baseMVA = new BaseMVA();
        baseMVA.setBaseMva(model.getBaseMva());
        if (networkId == null) {
            baseMVA.setNetwork(networkRepository.findByName(networkName).get());
        } else {
            baseMVA.setNetwork(networkRepository.findById(networkId).get());
        }

        return baseMVA;
    }

    /* createVLevels method
     * return a VLevels (made up of three values and networkId)
     */
    public static VoltageLevel createVLevels(MatpowerModel model, String networkName, Long networkId) {
        VoltageLevel vLevels = new VoltageLevel();
        vLevels.setv1(model.getvLevels().getv1());
        vLevels.setv2(model.getvLevels().getv2());
        vLevels.setv3(model.getvLevels().getv3());
        if (networkId == null) {
            vLevels.setNetwork(networkRepository.findByName(networkName).get());
        } else {
            vLevels.setNetwork(networkRepository.findById(networkId).get());
        }

        return vLevels;
    }

    /* createBuses method
     * return a list of buses
     * data is taken from MatpowerModel
     */
    public static List<Bus> createBuses(MatpowerModel model, String networkName, Long networkId) {
        Network network = null;
        if (networkId == null) {
            network = networkRepository.findByName(networkName).get();
        } else {
            network = networkRepository.findById(networkId).get();
        }

        List<Bus> buses = new ArrayList<>();
        for (int i = 0; i < model.getBuses().size(); ++i) {
            Bus bus1 = new Bus();
            bus1.setBusNum(model.getBuses().get(i).getBusNum());
            bus1.setType(model.getBuses().get(i).getType());
            bus1.setActivePower(model.getBuses().get(i).getActivePower());
            bus1.setReactivePower(model.getBuses().get(i).getReactivePower());
            bus1.setConductance(model.getBuses().get(i).getConductance());
            bus1.setSusceptance(model.getBuses().get(i).getSusceptance());
            bus1.setArea(model.getBuses().get(i).getArea());
            bus1.setVm(model.getBuses().get(i).getVm());
            bus1.setVa(model.getBuses().get(i).getVa());
            bus1.setBaseKv(model.getBuses().get(i).getBaseKv());
            bus1.setZone(model.getBuses().get(i).getZone());
            bus1.setVmax(model.getBuses().get(i).getVmax());
            bus1.setVmin(model.getBuses().get(i).getVmin());
            bus1.setNetwork(network);
            buses.add(bus1);
        }

        return buses;
    }

    public static List<BusExtension> createBusExtensions(MatpowerModel model, List<Bus> buses) {
        List<BusExtension> busExtensions = new ArrayList<>();
        for (int i = 0; i < model.getBusExtensions().size(); ++i) {
            BusExtension busExtension = new BusExtension();
            busExtension.setHasGen(model.getBusExtensions().get(i).getHasGen());
            busExtension.setIsLoad(model.getBusExtensions().get(i).getIsLoad());
            busExtension.setSnomMva(model.getBusExtensions().get(i).getSnomMva());
            busExtension.setSx(model.getBusExtensions().get(i).getSx());
            busExtension.setSy(model.getBusExtensions().get(i).getSy());
            busExtension.setGx(model.getBusExtensions().get(i).getGx());
            busExtension.setGy(model.getBusExtensions().get(i).getGy());
            busExtension.setBus(buses.get(i));
            busExtensions.add(busExtension);
        }

        return busExtensions;
    }

    /* createBranches method
     * return a list of branches
     * data is taken from MatpowerModel
     */
    public static List<Branch> createBranches(MatpowerModel model, String networkName, Long networkId, Bus lastBus) {
        Network network = null;
        if (networkId == null) {
            network = networkRepository.findByName(networkName).get();
        } else {
            network = networkRepository.findById(networkId).get();
        }

        /*long lastBusId = 0;
        if (lastBus != null) {
            lastBusId = lastBus.getBusId();
        }*/

        List<Branch> branches = new ArrayList<>();
        for (int i = 0; i < model.getBranches().size(); ++i) {
            Branch branch = new Branch();
            branch.setFbus(model.getBranches().get(i).getFbus());
            branch.setTbus(model.getBranches().get(i).getTbus());
            branch.setR(model.getBranches().get(i).getR());
            branch.setX(model.getBranches().get(i).getX());
            branch.setB(model.getBranches().get(i).getB());
            branch.setRatea(model.getBranches().get(i).getRatea());
            branch.setRateb(model.getBranches().get(i).getRateb());
            branch.setRatec(model.getBranches().get(i).getRatec());
            branch.setTapRatio(model.getBranches().get(i).getTapRatio());
            branch.setAngle(model.getBranches().get(i).getAngle());
            branch.setStatus(model.getBranches().get(i).getStatus());
            branch.setAngmin(model.getBranches().get(i).getAngmin());
            branch.setAngmax(model.getBranches().get(i).getAngmax());
            branch.setNetwork(network);
            branches.add(branch);
        }

        return branches;
    }

    /* createGenerators method
     * return a list of generators
     * data is taken from MatpowerModel
     */
    public static List<Generator> createGenerators(MatpowerModel model, String networkName, Long networkId, Bus lastBus) {
        Network network = null;
        if (networkId == null) {
            network = networkRepository.findByName(networkName).get();
        } else {
            network = networkRepository.findById(networkId).get();
        }

        /*long lastBusId = 0;
        if (lastBus != null) {
            lastBusId = lastBus.getBusId();
        }*/

        List<Generator> generators = new ArrayList<>();
        for (int i = 0; i < model.getGenerators().size(); ++i) {
            Generator generator = new Generator();
            generator.setBusNum(model.getGenerators().get(i).getBusNum());
            generator.setPg(model.getGenerators().get(i).getPg());
            generator.setQg(model.getGenerators().get(i).getQg());
            generator.setQmax(model.getGenerators().get(i).getQmax());
            generator.setQmin(model.getGenerators().get(i).getQmin());
            generator.setVg(model.getGenerators().get(i).getVg());
            generator.setmBase(model.getGenerators().get(i).getmBase());
            generator.setStatus(model.getGenerators().get(i).getStatus());
            generator.setPmax(model.getGenerators().get(i).getPmax());
            generator.setPmin(model.getGenerators().get(i).getPmin());
            generator.setPc1(model.getGenerators().get(i).getPc1());
            generator.setPc2(model.getGenerators().get(i).getPc2());
            generator.setQc1min(model.getGenerators().get(i).getQc1min());
            generator.setQc1max(model.getGenerators().get(i).getQc1max());
            generator.setQc2max(model.getGenerators().get(i).getQc2max());
            generator.setQc2min(model.getGenerators().get(i).getQc2min());
            generator.setRampAgc(model.getGenerators().get(i).getRampAgc());
            generator.setRamp10(model.getGenerators().get(i).getRamp10());
            generator.setRamp30(model.getGenerators().get(i).getRamp30());
            generator.setRampQ(model.getGenerators().get(i).getRampQ());
            generator.setApf(model.getGenerators().get(i).getApf());
            generator.setNetwork(network);
            generators.add(generator);
        }

        return generators;
    }

    /* createGenTags method
     * return a list of gen tags (genTag and genId)
     * data is taken from MatpowerModel
     */
    public static List<GenTag> createGenTags(MatpowerModel model, List<Generator> gens) {
        List<GenTag> genTags = new ArrayList<>();
        for (int i = 0; i < model.getGenTags().size(); ++i) {
            GenTag genTag = new GenTag();
            genTag.setGenerator(gens.get(i));
            genTag.setGenTag(model.getGenTags().get(i).getGenTag());
            genTags.add(genTag);
        }

        return genTags;
    }

    /* createBusNames method
     * return a list of bus names (busName and busId)
     * data is taken from MatpowerModel
     */
    public static List<BusName> createBusNames(MatpowerModel model, List<Bus> buses) {
        List<BusName> busNames = new ArrayList<>();
        for (int i = 0; i < model.getBusNames().size(); ++i) {
            BusName busName = new BusName();
            busName.setBus(buses.get(i));
            busName.setBusName(model.getBusNames().get(i).getBusName());
            busNames.add(busName);
        }

        return busNames;
    }

    /* createTransformers method
     * return a list of transformers
     * data is taken from MatpowerModel
     */
    public static List<Transformer> createTransformers(MatpowerModel model, String networkName, String networkId) {
        Network network = null;
        if (networkId == null) {
            network = networkRepository.findByName(networkName).get();
        } else {
            network = networkRepository.findById(networkId).get();
        }

        List<Transformer> trans = new ArrayList<>();
        for (int i = 0; i < model.getTransformers().size(); ++i) {
            Transformer tran = new Transformer();
            tran.setFbus(model.getTransformers().get(i).getFbus());
            tran.setTbus(model.getTransformers().get(i).getTbus());
            tran.setMin(model.getTransformers().get(i).getMin());
            tran.setMax(model.getTransformers().get(i).getMax());
            tran.setTotalTaps(model.getTransformers().get(i).getTotalTaps());
            tran.setTap(model.getTransformers().get(i).getTap());
            tran.setNetwork(network);
            trans.add(tran);
        }

        return trans;
    }

    /* createBusCoords method
     * return a list of bus coordinates
     * data is taken from MatpowerModel
     */
    public static List<BusCoordinate> createBusCoords(MatpowerModel model, List<Bus> buses) {
        List<BusCoordinate> busCoordinates = new ArrayList<>();
        for (int i = 0; i < model.getBusCoordinates().size(); ++i) {
            BusCoordinate bc = new BusCoordinate();
            bc.setBus(buses.get(i));
            bc.setX(model.getBusCoordinates().get(i).getX());
            bc.setY(model.getBusCoordinates().get(i).getY());
            busCoordinates.add(bc);
        }

        return busCoordinates;
    }

    /* createGenCosts method
     * return a list of gen costs
     * data is taken from MatpowerModel
     */
    public static List<GenCost> createGenCosts(MatpowerModel model, List<Generator> generators) {
        List<GenCost> genCosts = new ArrayList<>();
        for (int i = 0; i < model.getGenCosts().size(); ++i) {
            GenCost genCost = new GenCost();
            genCost.setGenerator(generators.get(i));
            genCost.setModel(model.getGenCosts().get(i).getModel());
            genCost.setStartup(model.getGenCosts().get(i).getStartup());
            genCost.setShutdown(model.getGenCosts().get(i).getShutdown());
            genCost.setnCost(model.getGenCosts().get(i).getnCost());

            /*genCost.setX1(model.getGenCosts().get(i).getX1());
            genCost.setY1(model.getGenCosts().get(i).getY1());
            genCost.setXnyn(model.getGenCosts().get(i).getXnyn());*/
            genCosts.add(genCost);
        }

        return genCosts;
    }

    /* createCapsBankData method
     * return a list of capacitors bank data
     * data is taken from MatpowerModel
     */
    public static List<CapacitorBankData> createCapsBankData(MatpowerModel model, String networkName, Long networkId) {
        Network network = null;
        if (networkId == null) {
            network = networkRepository.findByName(networkName).get();
        } else {
            network = networkRepository.findById(networkId).get();
        }

        List<CapacitorBankData> caps = new ArrayList<>();
        for (int i = 0; i < model.getCaps().size(); ++i) {
            CapacitorBankData cap = new CapacitorBankData();
            cap.setBusNum(model.getCaps().get(i).getBusNum());
            cap.setNodeId(model.getCaps().get(i).getNodeId());
            cap.setBankId(model.getCaps().get(i).getBankId());
            cap.setQnom(model.getCaps().get(i).getQnom());
            cap.setNetwork(network);
            caps.add(cap);
        }

        return caps;
    }

    /* createLoadElVarsP method
     * return a list of load P values
     * data is taken from MatpowerModel
     */
    public static List<LoadElVal> createLoadElVarsP(MatpowerModel model, List<Bus> buses) {
        List<LoadElVal> loadElVals = new ArrayList<>();
        for (int i = 0; i < model.getLoadElPVals().size(); ++i) {
            LoadElVal lev = new LoadElVal();
            lev.setBus(buses.get(i));
            lev.setQ(model.getLoadElPVals().get(i).getP());
            //TO DO Implementare....

            loadElVals.add(lev);
        }

        return loadElVals;
    }

    /** 
     * 23/11/2021  not yet used
    public static List<LoadElVal> createLoadElVal(MatpowerModel model, List<Bus> buses, LoadProfile loadProfile) throws Exception {
        List<LoadElVal> loadElValues = new ArrayList<>();
        for (Bus bus : buses) {
            for (int j = 0; j < model.getLoadElVarsP().size(); ++j) {
                LoadElVal loadElVal = new LoadElVal();
                loadElVal.setBusNum(bus.getBusNum());
                loadElVal.setTime(new SimpleDateFormat("HH:mm").parse(j + ":00"));
                loadElVal.setP(model.getLoadElVarsP().get(j).getT0());
                loadElVal.setQ(model.getLoadElVarsQ().get(j).getT0());
                loadElVal.setLoadProfile(loadProfile);
                loadElValues.add(loadElVal);
            }
        }
        return loadElValues;
    }*/

    /* createLoadElVarsQ method
     * return a list of load Q values
     * data is taken from MatpowerModel
     */
    public static List<LoadElVal> createLoadElVarsQ(MatpowerModel model, List<Bus> buses) {
        List<LoadElVal> loadElVals = new ArrayList<>();
        for (int i = 0; i < model.getLoadElQVals().size(); ++i) {
            LoadElVal lev = new LoadElVal();
            lev.setBus(buses.get(i));
            lev.setQ(model.getLoadElQVals().get(i).getQ());

            //TO DO Implementare
            loadElVals.add(lev);
        }

        return loadElVals;
    }

    public static InputStream exportData(
        @NotNull String networkName,
        @NotNull BaseMVA baseMVA,
        VoltageLevel vLevels,
        @NotNull List<Bus> buses,
        @NotNull List<Branch> branches,
        @NotNull List<Generator> generators,
        List<GenTag> genTags,
        List<CapacitorBankData> caps,
        List<GenCost> genCosts,
        List<Transformer> transformers,
        List<BusName> busNames,
        List<BusCoordinate> busCoordinates,
        List<LoadElVal> loadElValsP,
        List<LoadElVal> loadElValsQ
    ) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("function mpc = " + networkName + "\n");
        stringBuilder.append("\n");

        stringBuilder.append("%% MATPOWER Case Format : Version 2\n");
        stringBuilder.append("mpc.version = '2';\n");
        stringBuilder.append("\n");

        stringBuilder.append("% system MVA base\n");
        stringBuilder.append("mpc.baseMVA = " + baseMVA.getBaseMva() + ";\n");
        stringBuilder.append("\n");

        if (vLevels != null) {
            stringBuilder.append("%% system voltage levels (kV)\n");
            stringBuilder.append("mpc.V_levels = [\n");
            stringBuilder.append("\t" + vLevels.getv1() + "\t" + vLevels.getv2());
            if (vLevels.getv3() != 0.0) {
                stringBuilder.append("\t" + vLevels.getv3());
            }
            stringBuilder.append(";\n");
            stringBuilder.append("];\n");
            stringBuilder.append("\n");
        }

        stringBuilder.append("% bus data\n");
        stringBuilder.append("% bus_i\ttype Pd\t    Qd\t    Gs\tBs\tarea Vm\t   Va\tbaseKV\tzone Vmax\tVmin\n");
        stringBuilder.append("mpc.bus = [\n");
        for (int i = 0; i < buses.size(); ++i) {
            stringBuilder.append(
                "\t" +
                buses.get(i).getId() +
                "\t" +
                buses.get(i).getType() +
                "\t" +
                buses.get(i).getActivePower() +
                "\t" +
                buses.get(i).getReactivePower() +
                "\t" +
                buses.get(i).getConductance() +
                "\t" +
                buses.get(i).getSusceptance() +
                "\t" +
                buses.get(i).getArea() +
                "\t" +
                buses.get(i).getVm() +
                "\t" +
                buses.get(i).getVa() +
                "\t" +
                buses.get(i).getBaseKv() +
                "\t" +
                buses.get(i).getZone() +
                "\t" +
                buses.get(i).getVmax() +
                "\t" +
                buses.get(i).getVmin() +
                ";\n"
            );
        }
        stringBuilder.append("];\n");
        stringBuilder.append("\n");

        if (!caps.isEmpty()) {
            stringBuilder.append("%% CAPACITOR BANK Data\n");
            stringBuilder.append("%\tbus_i\tNode_ID\tbank_ID\tQnom (Mvar)\n");
            stringBuilder.append("mpc.capacitor_bank_dplan = [\n");
            for (CapacitorBankData cap : caps) {
                stringBuilder.append(
                    "\t" + cap.getBusNum() + "\t'" + cap.getNodeId() + "'" + "\t'" + cap.getBankId() + "'" + "\t" + cap.getQnom() + ";\n"
                );
            }
            stringBuilder.append("];\n");
            stringBuilder.append("\n");
        }

        stringBuilder.append("% generator data\n");
        stringBuilder.append(
            "% bus\tPg\t    Qg\t  Qmax Qmin\tVg   mBase status Pmax Pmin\tPc1\tPc2\tQc1min\tQc1max\tQc2min Qc2max ramp_acg\tramp_10\tramp_30\tramp_q  Apf\n"
        );
        stringBuilder.append("mpc.gen = [\n");
        for (Generator generator : generators) {
            stringBuilder.append(
                "\t" +
                generator.getBusNum() +
                "\t" +
                generator.getPg() +
                "\t" +
                generator.getQg() +
                "\t" +
                generator.getQmax() +
                "\t" +
                generator.getQmin() +
                "\t" +
                generator.getVg() +
                "\t" +
                generator.getmBase() +
                "\t" +
                generator.getStatus() +
                "\t" +
                generator.getPmax() +
                "\t" +
                generator.getPmin() +
                "\t" +
                generator.getPc1() +
                "\t" +
                generator.getPc2() +
                "\t" +
                generator.getQc1min() +
                "\t" +
                generator.getQc1max() +
                "\t" +
                generator.getQc2min() +
                "\t" +
                generator.getQc2max() +
                "\t" +
                generator.getRampAgc() +
                "\t" +
                generator.getRamp10() +
                "\t" +
                generator.getRamp30() +
                "\t" +
                generator.getRampQ() +
                "\t" +
                generator.getApf() +
                ";\n"
            );
        }
        stringBuilder.append("];\n");
        stringBuilder.append("\n");

        if (!genTags.isEmpty()) {
            stringBuilder.append(
                "%% gen tags \n" +
                "% Generation Technology Type:\n" +
                "%  CWS (Connection with Spain),\n" +
                "%  FOG (Fossil Gas),\n" +
                "%  FHC (Fossil Hard Coal),\n" +
                "%  HWR (Hydro Water Reservoir),\n" +
                "%  HPS (Hydro Pumped Storage),\n" +
                "%  HRP (Hydro Run-of-river and poundage),\n" +
                "%  SH1 (Small Hydro - P ≤ 10 MW),\n" +
                "%  SH3 (Small Hydro - 10 MW < P ≤ 30 MW),\n" +
                "%  PVP (Photovoltaic power plant),\n" +
                "%  WON (Wind onshore),\n" +
                "%  WOF (Wind offshore),\n" +
                "%  MAR (Marine),\n" +
                "%  OTH (Other thermal, such as biomass, biogas, Municipal solid waste and CHP renewable and non-renewable)\n" +
                "%\tgenType\n"
            );
            stringBuilder.append("mpc.gen_tags = [\n");
            for (GenTag genTag : genTags) {
                stringBuilder.append("\t'" + genTag.getGenTag() + "';\n");
            }
            stringBuilder.append("];\n");
            stringBuilder.append("\n");
        }

        stringBuilder.append("% branch data\n");
        stringBuilder.append("%fbus tbus\tr           x\t    b\t    rateA   B\tC ratio\tangle status angmin\tangmax\n");
        stringBuilder.append("mpc.branch = [\n");
        for (Branch branch : branches) {
            stringBuilder.append(
                "\t" +
                branch.getFbus() +
                "\t" +
                branch.getTbus() +
                "\t" +
                branch.getR() +
                "\t" +
                branch.getX() +
                "\t" +
                branch.getB() +
                "\t" +
                branch.getRatea() +
                "\t" +
                branch.getRateb() +
                "\t" +
                branch.getRatec() +
                "\t" +
                branch.getTapRatio() +
                "\t" +
                branch.getAngle() +
                "\t" +
                branch.getStatus() +
                "\t" +
                branch.getAngmin() +
                "\t" +
                branch.getAngmax() +
                ";\n"
            );
        }
        stringBuilder.append("];\n");
        stringBuilder.append("\n");

        if (!busNames.isEmpty()) {
            stringBuilder.append("%% bus names\n");
            stringBuilder.append("mpc.bus_name = [\n");
            for (BusName busName : busNames) {
                stringBuilder.append("\t'" + busName.getBusName() + "';\n");
            }
            stringBuilder.append("];\n");
            stringBuilder.append("\n");
        }

        if (!genCosts.isEmpty()) {
            stringBuilder.append("%% generator cost data\n");
            stringBuilder.append("%\t1\tstartup\tshutdown\tn\tx1\ty1\t...\txn\tyn\n");
            stringBuilder.append("%\t2\tstartup\tshutdown\tn\tc(n-1)\t...\tc0\n");
            stringBuilder.append("mpc.gencost = [\n");
            for (GenCost genCost : genCosts) {
                stringBuilder.append(
                    "\t" +
                    genCost.getModel() +
                    "\t" +
                    genCost.getStartup() +
                    "\t" +
                    genCost.getShutdown() +
                    "\t" +
                    genCost.getnCost() +
                    /*"\t" + genCost.getX1() +
                        "\t" + genCost.getY1() +
                        "\t" + genCost.getXnyn() +*/
                    ";\n"
                );
            }
            stringBuilder.append("];\n");
            stringBuilder.append("\n");
        }

        if (!transformers.isEmpty()) {
            stringBuilder.append("% mpc.trans\n");
            stringBuilder.append("% from bus\tto bus\tmin\tmax\ttotaltaps\ttap\n");
            stringBuilder.append("mpc.trans = [\n");
            for (Transformer transformer : transformers) {
                stringBuilder.append(
                    "\t" +
                    transformer.getFbus() +
                    "\t" +
                    transformer.getTbus() +
                    "\t" +
                    transformer.getMin() +
                    "\t" +
                    transformer.getMax() +
                    "\t" +
                    transformer.getTotalTaps() +
                    "\t" +
                    transformer.getTap() +
                    ";\n"
                );
            }
            stringBuilder.append("];\n");
            stringBuilder.append("\n");
        }

        if (!busCoordinates.isEmpty()) {
            stringBuilder.append("% Coordinates for each bus in the network\n");
            stringBuilder.append("mpc.coordinates = [\n");
            for (BusCoordinate bc : busCoordinates) {
                stringBuilder.append("\t" + bc.getX() + "\t" + bc.getY() + ";\n");
            }
            stringBuilder.append("];\n");
            stringBuilder.append("\n");
        }

        /*
        if (!loadElVarsP.isEmpty()) {
            stringBuilder.append("mpc.demandP = [\n");
            for (LoadElVar lev : loadElVarsP) {
                stringBuilder.append("\t" + lev.getT0() +
                        "\t" + lev.getT1() +
                        "\t" + lev.getT2() +
                        "\t" + lev.getT3() +
                        "\t" + lev.getT4() +
                        "\t" + lev.getT5() +
                        "\t" + lev.getT6() +
                        "\t" + lev.getT7() +
                        "\t" + lev.getT8() +
                        "\t" + lev.getT9() +
                        "\t" + lev.getT10() +
                        "\t" + lev.getT11() +
                        "\t" + lev.getT12() +
                        "\t" + lev.getT13() +
                        "\t" + lev.getT14() +
                        "\t" + lev.getT15() +
                        "\t" + lev.getT16() +
                        "\t" + lev.getT17() +
                        "\t" + lev.getT18() +
                        "\t" + lev.getT19() +
                        "\t" + lev.getT20() +
                        "\t" + lev.getT21() +
                        "\t" + lev.getT22() +
                        "\t" + lev.getT23() +
                        ";\n");
            }
            stringBuilder.append("];\n");
            stringBuilder.append("\n");
        }

        if (!loadElVarsQ.isEmpty()) {
            stringBuilder.append("mpc.demandQ = [\n");
            for (LoadElVar lev : loadElVarsQ) {
                stringBuilder.append("\t" + lev.getT0() +
                        "\t" + lev.getT1() +
                        "\t" + lev.getT2() +
                        "\t" + lev.getT3() +
                        "\t" + lev.getT4() +
                        "\t" + lev.getT5() +
                        "\t" + lev.getT6() +
                        "\t" + lev.getT7() +
                        "\t" + lev.getT8() +
                        "\t" + lev.getT9() +
                        "\t" + lev.getT10() +
                        "\t" + lev.getT11() +
                        "\t" + lev.getT12() +
                        "\t" + lev.getT13() +
                        "\t" + lev.getT14() +
                        "\t" + lev.getT15() +
                        "\t" + lev.getT16() +
                        "\t" + lev.getT17() +
                        "\t" + lev.getT18() +
                        "\t" + lev.getT19() +
                        "\t" + lev.getT20() +
                        "\t" + lev.getT21() +
                        "\t" + lev.getT22() +
                        "\t" + lev.getT23() +
                        ";\n");
            }
           
            stringBuilder.append("];\n");
            stringBuilder.append("\n");
        }
**/
        return new ByteArrayInputStream(stringBuilder.toString().getBytes());
    }
}
