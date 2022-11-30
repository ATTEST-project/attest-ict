package com.attest.ict.custom.diagrams.draw;

import com.attest.ict.custom.diagrams.diagram.ZoneDiagram;
import com.attest.ict.custom.diagrams.layout.ForceZoneLayoutFactory;
import com.attest.ict.custom.diagrams.writer.CustomSVGWriter;
import com.powsybl.commons.PowsyblException;
import com.powsybl.iidm.network.Identifiable;
import com.powsybl.iidm.network.Network;
import com.powsybl.iidm.network.Substation;
import com.powsybl.iidm.network.VoltageLevel;
import com.powsybl.sld.GraphBuilder;
import com.powsybl.sld.NetworkGraphBuilder;
import com.powsybl.sld.SubstationDiagram;
import com.powsybl.sld.VoltageLevelDiagram;
import com.powsybl.sld.layout.*;
import com.powsybl.sld.library.ComponentLibrary;
import com.powsybl.sld.library.ResourcesComponentLibrary;
import com.powsybl.sld.svg.DefaultDiagramLabelProvider;
import com.powsybl.sld.svg.DefaultDiagramStyleProvider;
import com.powsybl.sld.svg.DefaultSVGWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleLineDiagramTool {

    static final Logger LOGGER = LoggerFactory.getLogger(SingleLineDiagramTool.class);

    /* SvgGenerationConfig class
     * configuration class useful for creating svg files (use of standard resources in this case)
     */
    static class SvgGenerationConfig {

        ComponentLibrary componentLibrary = new ResourcesComponentLibrary("Convergence", "/ConvergenceLibrary");

        LayoutParameters parameters = new LayoutParameters().setCssLocation(LayoutParameters.CssLocation.INSERTED_IN_SVG);

        VoltageLevelLayoutFactory voltageLevelLayoutFactory;

        SubstationLayoutFactory substationLayoutFactory = new HorizontalSubstationLayoutFactory();

        SvgGenerationConfig(Network network) {
            voltageLevelLayoutFactory = new SmartVoltageLevelLayoutFactory(network);
        }
    }

    /* getSvgFile method
     * return the path of svg file
     */
    private static Path getSvgFile(Path outputDir, Identifiable identifiable, Network network) {
        try {
            return outputDir.resolve(
                network.getSourceFormat() + "_" + URLEncoder.encode(identifiable.getId(), StandardCharsets.UTF_8.name()) + ".svg"
            );
        } catch (UnsupportedEncodingException e) {
            throw new PowsyblException(e);
        }
    }

    /* generateVoltageLevelSvg method
     * generate svg files for only voltage levels
     */
    private static void generateVoltageLevelSvg(
        Path outputDir,
        SvgGenerationConfig generationConfig,
        VoltageLevel vl,
        GraphBuilder graphBuilder,
        Network network
    ) {
        Path svgFile = getSvgFile(outputDir, vl, network);
        LOGGER.info("Generating '" + svgFile + "' (" + vl.getNominalV() + ")");
        try {
            VoltageLevelDiagram
                .build(graphBuilder, vl.getId(), generationConfig.voltageLevelLayoutFactory, true)
                .writeSvg(
                    "",
                    generationConfig.componentLibrary,
                    generationConfig.parameters,
                    new DefaultDiagramLabelProvider(network, generationConfig.componentLibrary, generationConfig.parameters),
                    new DefaultDiagramStyleProvider(),
                    svgFile
                );
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    /* generateSubstationSvg method
     * generate svg files for substations
     */
    private static void generateSubstationSvg(
        Path outputDir,
        SvgGenerationConfig generationConfig,
        Substation s,
        GraphBuilder graphBuilder,
        Network network
    ) {
        Path svgFile = getSvgFile(outputDir, s, network);
        LOGGER.info("Generating '" + svgFile + "'");
        try {
            SubstationDiagram
                .build(graphBuilder, s.getId(), generationConfig.substationLayoutFactory, generationConfig.voltageLevelLayoutFactory, true)
                .writeSvg(
                    "",
                    generationConfig.componentLibrary,
                    generationConfig.parameters,
                    new DefaultDiagramLabelProvider(network, generationConfig.componentLibrary, generationConfig.parameters),
                    new DefaultDiagramStyleProvider(),
                    svgFile
                );
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    /* generateAll method
     * generate svg files for substations and voltage levels
     */
    private static void generateAll(
        Path outputDir,
        GraphBuilder graphBuilder,
        Network network,
        SvgGenerationConfig generationConfig,
        boolean onlySubstations
    ) {
        // export all substations
        for (Substation s : network.getSubstations()) {
            generateSubstationSvg(outputDir, generationConfig, s, graphBuilder, network);
        }
        if (!onlySubstations) {
            // export all voltage levels
            for (VoltageLevel vl : network.getVoltageLevels()) {
                generateVoltageLevelSvg(outputDir, generationConfig, vl, graphBuilder, network);
            }
        }
    }

    /* draw method (public)
     * invoke this method in other classes to export all substations and voltage levels in svg files
     */
    public static void draw(Network network, String folderPath, boolean onlySubstations) {
        GraphBuilder graphBuilder = new NetworkGraphBuilder(network);
        SvgGenerationConfig generationConfig = new SvgGenerationConfig(network);

        generateAll(Paths.get(folderPath), graphBuilder, network, generationConfig, onlySubstations);
    }

    public static String createSubstationDiagram(Network network, Substation substation, GraphBuilder graphBuilder) {
        SvgGenerationConfig config = new SvgGenerationConfig(network);
        //        GraphBuilder graphBuilder = new NetworkGraphBuilder(network);

        String svgData = null;

        try (
            StringWriter svgWriter = new StringWriter();
            StringWriter metadataWriter = new StringWriter();
            StringWriter jsonWriter = new StringWriter()
        ) {
            SubstationDiagram diagram = SubstationDiagram.build(
                graphBuilder,
                substation.getId(),
                config.substationLayoutFactory,
                config.voltageLevelLayoutFactory,
                true
            );
            diagram.writeSvg(
                "",
                new CustomSVGWriter(config.componentLibrary, config.parameters),
                new DefaultDiagramLabelProvider(network, config.componentLibrary, config.parameters),
                new DefaultDiagramStyleProvider(),
                svgWriter,
                metadataWriter
            );
            diagram.getSubGraph().writeJson(jsonWriter);

            svgWriter.flush();
            metadataWriter.flush();
            svgData = svgWriter.toString();
        } catch (Exception e) {
            LOGGER.error("Exception: ", e);
        }

        return svgData;
    }

    public static Map<String, String> createSubstationDiagramMetadata(Network network, Substation substation, GraphBuilder graphBuilder) {
        Map<String, String> svgMap = new HashMap<>();

        SvgGenerationConfig config = new SvgGenerationConfig(network);
        //        GraphBuilder graphBuilder = new NetworkGraphBuilder(network);

        String svgData = null;
        String metaData = null;

        try (
            StringWriter svgWriter = new StringWriter();
            StringWriter metadataWriter = new StringWriter();
            StringWriter jsonWriter = new StringWriter()
        ) {
            SubstationDiagram diagram = SubstationDiagram.build(
                graphBuilder,
                substation.getId(),
                config.substationLayoutFactory,
                config.voltageLevelLayoutFactory,
                true
            );
            diagram.writeSvg(
                "",
                new CustomSVGWriter(config.componentLibrary, config.parameters),
                new DefaultDiagramLabelProvider(network, config.componentLibrary, config.parameters),
                new DefaultDiagramStyleProvider(),
                svgWriter,
                metadataWriter
            );
            diagram.getSubGraph().writeJson(jsonWriter);

            svgWriter.flush();
            metadataWriter.flush();
            svgData = svgWriter.toString();
            /* svgData =
                svgData.replace(
                    "<svg xmlns=\"http://www.w3.org/2000/svg\">",
                    "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100%\" height=\"100%\" viewBox=\"0 0 3200 1000\">"
                ); */
            metaData = metadataWriter.toString();
        } catch (Exception e) {
            LOGGER.error("Exception: ", e);
        }

        svgMap.put("svg", svgData);
        svgMap.put("metadata", metaData);

        return svgMap;
    }

    public static String createEntireNetwork(Network network) {
        SvgGenerationConfig generationConfig = new SvgGenerationConfig(network);
        GraphBuilder graphBuilder = new NetworkGraphBuilder(network);

        List<String> substationsIds = network.getSubstationStream().map(Identifiable::getId).collect(Collectors.toList());

        String svgData = null;

        try (
            StringWriter svgWriter = new StringWriter();
            StringWriter metadataWriter = new StringWriter();
            StringWriter jsonWriter = new StringWriter()
        ) {
            ZoneDiagram diagram = ZoneDiagram.build(
                graphBuilder,
                substationsIds,
                new ForceZoneLayoutFactory(ForceSubstationLayoutFactory.CompactionType.NONE),
                new HorizontalSubstationLayoutFactory(),
                new SmartVoltageLevelLayoutFactory(network),
                true
            );
            diagram.writeSvg(
                "",
                new CustomSVGWriter(generationConfig.componentLibrary, generationConfig.parameters),
                new DefaultDiagramLabelProvider(network, generationConfig.componentLibrary, generationConfig.parameters),
                new DefaultDiagramStyleProvider(),
                svgWriter,
                metadataWriter
            );
            diagram.getZoneGraph().writeJson(jsonWriter);

            svgData = svgWriter.toString();
        } catch (IOException e) {
            LOGGER.error("Exception: ", e);
        }

        return svgData;
    }

    public static Map<String, String> createEntireNetworkMetadata(Network network) {
        Map<String, String> svgMap = new HashMap<>();

        SvgGenerationConfig generationConfig = new SvgGenerationConfig(network);
        GraphBuilder graphBuilder = new NetworkGraphBuilder(network);

        List<String> substationsIds = network.getSubstationStream().map(Identifiable::getId).collect(Collectors.toList());

        String svgData = null;
        String metaData = null;

        try (
            StringWriter svgWriter = new StringWriter();
            StringWriter metadataWriter = new StringWriter();
            StringWriter jsonWriter = new StringWriter()
        ) {
            ZoneDiagram diagram = ZoneDiagram.build(
                graphBuilder,
                substationsIds,
                new ForceZoneLayoutFactory(ForceSubstationLayoutFactory.CompactionType.NONE),
                new HorizontalSubstationLayoutFactory(),
                new SmartVoltageLevelLayoutFactory(network),
                true
            );
            diagram.writeSvg(
                "",
                new CustomSVGWriter(generationConfig.componentLibrary, generationConfig.parameters),
                new DefaultDiagramLabelProvider(network, generationConfig.componentLibrary, generationConfig.parameters),
                new DefaultDiagramStyleProvider(),
                svgWriter,
                metadataWriter
            );
            diagram.getZoneGraph().writeJson(jsonWriter);

            svgWriter.flush();
            metadataWriter.flush();
            svgData = svgWriter.toString();
            metaData = metadataWriter.toString();
        } catch (IOException e) {
            LOGGER.error("Exception: ", e);
        }

        svgMap.put("svg", svgData);
        svgMap.put("metadata", metaData);

        return svgMap;
    }
}
