package com.attest.ict.helper.matpower.network.reader;

import com.attest.ict.custom.model.matpower.MatpowerModel;
import com.attest.ict.helper.matpower.common.reader.MatpowerReader;
import com.attest.ict.helper.matpower.network.annotated.*;
import com.attest.ict.helper.matpower.network.util.MatpowerAttributesTemplate;
import com.attest.ict.helper.matpower.network.util.MatpowerCaseStruct;
import com.attest.ict.helper.matpower.network.util.MatpowerNetworkSection;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class MatpowerNetworkReader extends MatpowerReader {

    public static final Logger LOGGER = LoggerFactory.getLogger(MatpowerNetworkReader.class);

    // Matpower supported version
    public static final String MATPOWER_SUPPORTED_VERSION = "2";

    private MatpowerNetworkReader() {}

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
     * read file passing InputStream, other 'read' method will be called with BufferedReader parameter
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

        MatpowerModel model = null;
        MatpowerNetworkSection section = null;
        MatpowerCaseStruct busStruct = null;
        MatpowerCaseStruct generatorStruct = null;
        MatpowerCaseStruct branchStruct = null;
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
            } else if (line.contains("% bus data") || line.contains("%\tbus data")) {
                busStruct = new MatpowerCaseStruct(MatpowerNetworkSection.BUS);
                createStruct(reader, busStruct);
                parseBusesLines(model, busStruct);
            } else if (line.toLowerCase().contains("% generator data") || line.toLowerCase().contains("%\tgenerator data")) {
                generatorStruct = new MatpowerCaseStruct(MatpowerNetworkSection.GENERATOR);
                createStruct(reader, generatorStruct);
                parseGeneratorsLines(model, generatorStruct);
            } else if (line.contains("% branch data") || line.contains("%\tbranch data")) {
                branchStruct = new MatpowerCaseStruct(MatpowerNetworkSection.BRANCH);
                createStruct(reader, branchStruct);
                parseBranchesLines(model, branchStruct);
            } else if (line.startsWith("mpc.gen_tags =") || line.startsWith("mpc.gen_tags=")) {
                section = MatpowerNetworkSection.GEN_TAGS;
            } else if (line.startsWith("mpc.bus_name =") || line.startsWith("mpc.bus_name=")) {
                section = MatpowerNetworkSection.BUS_NAMES;
            } else if ((line.startsWith("mpc.trans =") || line.startsWith("mpc.trans=")) && (line.contains("];") || line.contains("};"))) {
                section = MatpowerNetworkSection.TRANSFORMER;
                int firstSB = line.indexOf("[");
                int secondSB = line.indexOf("]");
                lines.add("\t" + line.substring(firstSB + 1, secondSB) + ";");
                section = processEndSection(model, section, lines);
            } else if (line.startsWith("mpc.trans =") || line.startsWith("mpc.trans=")) {
                section = MatpowerNetworkSection.TRANSFORMER;
            } else if (line.startsWith("mpc.coordinates =") || line.startsWith("mpc.coordinates=")) {
                section = MatpowerNetworkSection.BUS_COORDINATES;
            } else if (line.startsWith("mpc.gencost =") || line.startsWith("mpc.gencost=")) {
                section = MatpowerNetworkSection.GEN_COST;
            } else if (line.startsWith("mpc.capacitor_bank_dplan =") || line.startsWith("mpc.capacitor_bank_dplan=")) {
                section = MatpowerNetworkSection.CAP_BANK_DATA;
            } else if (line.startsWith("mpc.V_levels =") || line.startsWith("mpc.V_levels=")) {
                section = MatpowerNetworkSection.V_LEVELS;
            }/*else if (line.startsWith("mpc.demandP =") || line.startsWith("mpc.demandP=")) {
                section = MatpowerNetworkSection.LOAD_EL_VAR_P;
            }
            else if (line.startsWith("mpc.demandQ =") || line.startsWith("mpc.demandQ=")) {
                section = MatpowerNetworkSection.LOAD_EL_VAR_Q;
            }*/ else if (line.contains("];") || line.contains("};")) {
                section = processEndSection(model, section, lines);
            } else {
                if (section != null) {
                    lines.add(line);
                }
            }
        }

        return model;
    }

    private static void createStruct(BufferedReader reader, MatpowerCaseStruct struct) throws IOException {
        List<String> data = new ArrayList<>();
        String line = reader.readLine(); // header
        if (struct.getType().equals(MatpowerNetworkSection.BRANCH)) {
            while (!(line.startsWith("% fbus") || line.startsWith("%\tfbus") || line.startsWith("%fbus"))) {
                line = reader.readLine();
            }
        }
        struct.setAttributes(parseAttributes(line, struct));
        line = reader.readLine(); // mpc.bus/branch/generator = [
        line = reader.readLine(); // first matrix row
        while (!(line.contains("];"))) {
            data.add(line);
            line = reader.readLine();
        }
        struct.setMatrix(data);
    }

    /* canSkipLine method
     */
    private static boolean canSkipLine(String line) {
        if (line.startsWith("%% bus data") || line.startsWith("%%\tbus data")) {
            return false;
        }
        if (line.startsWith("%% generator data") || line.startsWith("%%\tgenerator data")) {
            return false;
        }
        if (line.startsWith("%% branch data") || line.startsWith("%%\tbranch data")) {
            return false;
        }
        return line.startsWith("%%") || line.trim().length() == 0;
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

    /* processMatlabAssignment method
     */
    private static String processMatlabAssignment(String str) {
        Objects.requireNonNull(str);
        String str2 = str.replace(';', ' ');
        final StringTokenizer st = new StringTokenizer(str2, " ");
        st.nextToken(); // mpc.XYZ
        st.nextToken(); // =
        return st.nextToken();
    }

    /* processBaseMva method
     * parse and save BaseMVA value
     */
    private static void processBaseMva(String line, MatpowerModel model) {
        double baseMva = Double.parseDouble(processMatlabAssignment(line));
        model.setBaseMva(baseMva);
    }

    /* processEndSection method
     * data from a specific section is parsed using parseLines method
     */
    private static MatpowerNetworkSection processEndSection(MatpowerModel model, MatpowerNetworkSection section, List<String> lines) {
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
    private static void parseLines(List<String> lines, MatpowerModel model, MatpowerNetworkSection section) {
        switch (section) {
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

    private static List<String> parseAttributes(String line, MatpowerCaseStruct struct) {
        String fixedLine = line
            .replaceAll("^%", "% ")
            .replaceAll("\\s+", "\t")
            .replaceAll("\t\\(", " (")
            .replaceAll("tap\tratio", "tap ratio")
            .replaceAll("shift\tangle", "shift angle")
            .trim();
        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        TsvParser parser = new TsvParser(settings);
        String[] attributes = parser.parseLine(fixedLine);
        List<String> attributesList = Arrays
            .stream(attributes)
            .filter(Objects::nonNull)
            .filter(e -> !e.contains("%"))
            .collect(Collectors.toList());
        attributesList = getAttributesList(attributesList.size(), struct);
        return attributesList;
    }

    /*private static List<String> fixAttributesNames(List<String> attributes, MatpowerCaseStruct struct) {
        List<String> standardAttributes = null;
        int attributesSize = attributes.size();
        for (int i=0; i<standardAttributes.size(); ++i) {
            if (!attributes.get(i).equals(standardAttributes.get(i))) {
                attributes.set(i, standardAttributes.get(i));
            }
        }
        return getAttributesList(attributesSize, struct);
    }*/

    private static List<String> getAttributesList(int attributesSize, MatpowerCaseStruct struct) {
        List<String> attributesList = null;
        if (struct.getType().equals(MatpowerNetworkSection.BUS)) {
            if (MatpowerAttributesTemplate.BUS_EXTENSION_1.size() == attributesSize) {
                attributesList = MatpowerAttributesTemplate.BUS_EXTENSION_1;
            } else if (MatpowerAttributesTemplate.BUS_EXTENSION_2.size() == attributesSize) {
                attributesList = MatpowerAttributesTemplate.BUS_EXTENSION_2;
            } else {
                attributesList = MatpowerAttributesTemplate.BUS_STANDARD;
            }
        } else if (struct.getType().equals(MatpowerNetworkSection.BRANCH)) {
            if (MatpowerAttributesTemplate.BRANCH_EXTENSION_1.size() == attributesSize) {
                attributesList = MatpowerAttributesTemplate.BRANCH_EXTENSION_1;
            } else if (MatpowerAttributesTemplate.BRANCH_EXTENSION_2.size() == attributesSize) {
                attributesList = MatpowerAttributesTemplate.BRANCH_EXTENSION_2;
            } else if (MatpowerAttributesTemplate.BRANCH_EXTENSION_3.size() == attributesSize) {
                attributesList = MatpowerAttributesTemplate.BRANCH_EXTENSION_3;
            } else {
                attributesList = MatpowerAttributesTemplate.BRANCH_STANDARD;
            }
        } else if (struct.getType().equals(MatpowerNetworkSection.GENERATOR)) {
            if (MatpowerAttributesTemplate.GENERATOR_EXTENSION_1.size() == attributesSize) {
                attributesList = MatpowerAttributesTemplate.GENERATOR_EXTENSION_1;
            } else if (MatpowerAttributesTemplate.GENERATOR_EXTENSION_2.size() == attributesSize) {
                attributesList = MatpowerAttributesTemplate.GENERATOR_EXTENSION_2;
            } else {
                attributesList = MatpowerAttributesTemplate.GENERATOR_STANDARD;
            }
        }
        return attributesList;
    }

    private static void parseBusesLines(MatpowerModel model, MatpowerCaseStruct struct) {
        List<String> lines = struct.getMatrix();

        // parse standard buses attributes
        model.getBuses().addAll(parseLines(lines, BusAnnotated.class));

        // parse additional buses attributes (template 1)
        if (struct.getAttributes().equals(MatpowerAttributesTemplate.BUS_EXTENSION_1)) {
            int extensionIndex = struct.getAttributes().indexOf("hasgen");
            List<String> busExtensionsList = getStructExtension(extensionIndex, lines);
            model.getBusExtensions().addAll(parseLines(busExtensionsList, BusExtensionAnnotated.class));
        } else if (struct.getAttributes().equals(MatpowerAttributesTemplate.BUS_EXTENSION_2)) {
            int extensionIndex = struct.getAttributes().indexOf("status");
            List<String> busExtensionsList = getStructExtension(extensionIndex, lines);
            model.getBusExtensions().addAll(parseLines(busExtensionsList, BusExtensionT41Annotated.class));
        }
    }

    private static void parseBranchesLines(MatpowerModel model, MatpowerCaseStruct struct) {
        List<String> lines = struct.getMatrix();

        // parse standard buses attributes
        model.getBranches().addAll(parseLines(lines, BranchAnnotated.class));

        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator(";");
        TsvParser parser = new TsvParser(settings);

        // fix for parsing additional values with different number between header with attributes and rows with values
        String firstRow = struct.getMatrix().get(0);
        String[] firstRowParsed = Arrays.stream(parser.parseLine(firstRow)).filter(Objects::nonNull).toArray(String[]::new);
        int realBranchLineLength = firstRowParsed.length;

        if (struct.getAttributes().size() == realBranchLineLength) {
            // parse additional buses attributes (template 1)
            if (struct.getAttributes().equals(MatpowerAttributesTemplate.BRANCH_EXTENSION_1)) {
                int extensionIndex = struct.getAttributes().indexOf("step_size");
                List<String> branchExtensionsList = getStructExtension(extensionIndex, lines);
                model.getBranchExtensions().addAll(parseLines(branchExtensionsList, BranchExtension1Annotated.class));
            }
            if (struct.getAttributes().equals(MatpowerAttributesTemplate.BRANCH_EXTENSION_2)) {
                int extensionIndex = struct.getAttributes().indexOf("step_size");
                List<String> branchExtensionsList = getStructExtension(extensionIndex, lines);
                model.getBranchExtensions().addAll(parseLines(branchExtensionsList, BranchExtensionAnnotated.class));
            }
            if (struct.getAttributes().equals(MatpowerAttributesTemplate.BRANCH_EXTENSION_3)) {
                int extensionIndex = struct.getAttributes().indexOf("g");
                List<String> branchExtensionsList = getStructExtension(extensionIndex, lines);
                model.getBranchExtensions().addAll(parseLines(branchExtensionsList, BranchExtension2Annotated.class));
            }
        }
    }

    private static void parseGeneratorsLines(MatpowerModel model, MatpowerCaseStruct struct) {
        List<String> lines = struct.getMatrix();

        // parse standard buses attributes
        model.getGenerators().addAll(parseLines(lines, GeneratorAnnotated.class));

        // parse additional buses attributes (template 1)
        if (struct.getAttributes().equals(MatpowerAttributesTemplate.GENERATOR_EXTENSION_1)) {
            int extensionIndex = struct.getAttributes().indexOf("id");
            List<String> generatorExtensionsList = getStructExtension(extensionIndex, lines);
            model.getGeneratorExtensions().addAll(parseLines(generatorExtensionsList, GeneratorExtensionAnnotated.class));
        }
        if (struct.getAttributes().equals(MatpowerAttributesTemplate.GENERATOR_EXTENSION_2)) {
            int extensionIndex = struct.getAttributes().indexOf("status_curt");
            List<String> generatorExtensionsList = getStructExtension(extensionIndex, lines);
            model.getGeneratorExtensions().addAll(parseLines(generatorExtensionsList, GeneratorExtension1Annotated.class));
        }
    }

    private static List<String> getStructExtension(int extensionIndex, List<String> lines) {
        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        TsvParser parser = new TsvParser(settings);

        List<String> extensionsList = lines
            .stream()
            .map(MatpowerReader::cleanLine)
            .map(parser::parseLine)
            .map(e -> Arrays.asList(e).subList(extensionIndex, e.length))
            .map(e -> String.join("\t", e).replaceAll("null", ""))
            .collect(Collectors.toList());

        return extensionsList;
    }
}
