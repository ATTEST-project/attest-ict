package com.attest.ict.custom.diagrams.diagram;

import com.powsybl.commons.PowsyblException;
import com.powsybl.sld.GraphBuilder;
import com.powsybl.sld.layout.SubstationLayoutFactory;
import com.powsybl.sld.layout.VoltageLevelLayoutFactory;
import com.powsybl.sld.layout.ZoneLayout;
import com.powsybl.sld.layout.ZoneLayoutFactory;
import com.powsybl.sld.model.ZoneGraph;
import com.powsybl.sld.svg.DiagramLabelProvider;
import com.powsybl.sld.svg.DiagramStyleProvider;
import com.powsybl.sld.svg.GraphMetadata;
import com.powsybl.sld.svg.SVGWriter;
import java.io.Writer;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZoneDiagram {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZoneDiagram.class);

    private final ZoneGraph zoneGraph;

    private final ZoneLayout zoneLayout;

    private ZoneDiagram(ZoneGraph graph, ZoneLayout layout) {
        this.zoneGraph = Objects.requireNonNull(graph);
        this.zoneLayout = Objects.requireNonNull(layout);
    }

    public static ZoneDiagram build(
        GraphBuilder graphBuilder,
        List<String> substationsIds,
        ZoneLayoutFactory zLayoutFactory,
        SubstationLayoutFactory sLayoutFactory,
        VoltageLevelLayoutFactory vLayoutFactory,
        boolean useName
    ) {
        Objects.requireNonNull(graphBuilder);
        Objects.requireNonNull(zLayoutFactory);
        Objects.requireNonNull(vLayoutFactory);

        if (substationsIds.isEmpty()) {
            throw new PowsyblException("Zone without any substation");
        }

        ZoneGraph graph = graphBuilder.buildZoneGraph(substationsIds, useName);

        ZoneLayout layout = zLayoutFactory.create(graph, sLayoutFactory, vLayoutFactory);

        return new ZoneDiagram(graph, layout);
    }

    public ZoneGraph getZoneGraph() {
        return zoneGraph;
    }

    public void writeSvg(
        String prefixId,
        SVGWriter writer,
        DiagramLabelProvider nodeLabelConfiguration,
        DiagramStyleProvider styleProvider,
        Writer svgWriter,
        Writer metadataWriter
    ) {
        Objects.requireNonNull(writer);
        Objects.requireNonNull(writer.getLayoutParameters());
        Objects.requireNonNull(svgWriter);
        Objects.requireNonNull(metadataWriter);

        zoneLayout.run(writer.getLayoutParameters());

        // write SVG file
        LOGGER.info("Writing SVG and JSON metadata files...");

        GraphMetadata metadata = writer.write(prefixId, zoneGraph, nodeLabelConfiguration, styleProvider, svgWriter);

        // write metadata file
        metadata.writeJson(metadataWriter);
    }
}
