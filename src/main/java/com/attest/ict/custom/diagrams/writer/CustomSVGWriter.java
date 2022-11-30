package com.attest.ict.custom.diagrams.writer;

import static com.powsybl.sld.svg.DiagramStyles.escapeClassName;
import static com.powsybl.sld.svg.DiagramStyles.escapeId;

import com.powsybl.sld.layout.LayoutParameters;
import com.powsybl.sld.library.AnchorPointProvider;
import com.powsybl.sld.library.ComponentLibrary;
import com.powsybl.sld.model.Edge;
import com.powsybl.sld.model.VoltageLevelGraph;
import com.powsybl.sld.svg.*;
import java.util.List;
import org.w3c.dom.Element;

public class CustomSVGWriter extends DefaultSVGWriter {

    public CustomSVGWriter(ComponentLibrary componentLibrary, LayoutParameters layoutParameters) {
        super(componentLibrary, layoutParameters);
    }

    private static String getWireId(String prefixId, String containerId, Edge edge) {
        return escapeClassName(prefixId + "_" + containerId + "_" + edge.getNode1().getId() + "_" + edge.getNode2().getId());
    }

    @Override
    protected void drawEdges(
        String prefixId,
        Element root,
        VoltageLevelGraph graph,
        List<Edge> edges,
        GraphMetadata metadata,
        AnchorPointProvider anchorPointProvider,
        DiagramLabelProvider initProvider,
        DiagramStyleProvider styleProvider
    ) {
        String voltageLevelId = graph.getVoltageLevelInfos().getId();

        for (Edge edge : edges) {
            String wireId = getWireId(prefixId, voltageLevelId, edge);

            Element g = root.getOwnerDocument().createElement(GROUP);
            g.setAttribute("id", wireId);
            List<String> wireStyles = styleProvider.getSvgWireStyles(edge, layoutParameters.isHighlightLineState());
            g.setAttribute(CLASS, String.join(" ", wireStyles));

            root.appendChild(g);

            Element polyline = root.getOwnerDocument().createElement(POLYLINE);
            WireConnection anchorPoints = WireConnection.searchBetterAnchorPoints(anchorPointProvider, edge.getNode1(), edge.getNode2());

            // Determine points of the polyline
            List<Double> pol = anchorPoints.calculatePolylinePoints(
                edge.getNode1(),
                edge.getNode2(),
                layoutParameters.isDrawStraightWires()
            );

            polyline.setAttribute(POINTS, pointsListToString(pol));
            g.appendChild(polyline);

            metadata.addWireMetadata(
                new GraphMetadata.WireMetadata(
                    wireId,
                    escapeId(edge.getNode1().getId()),
                    escapeId(edge.getNode2().getId()),
                    layoutParameters.isDrawStraightWires(),
                    false
                )
            );
        }
    }
}
