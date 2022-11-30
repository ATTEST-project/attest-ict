package com.attest.ict.custom.diagrams.draw;

import com.powsybl.iidm.network.Network;
import com.powsybl.iidm.network.Substation;
import com.powsybl.sld.NetworkGraphBuilder;
import com.powsybl.sld.SubstationDiagram;
import com.powsybl.sld.layout.LayoutParameters;
import com.powsybl.sld.library.ComponentLibrary;
import com.powsybl.sld.library.ResourcesComponentLibrary;
import com.powsybl.sld.svg.DefaultDiagramLabelProvider;
import com.powsybl.sld.svg.DefaultDiagramStyleProvider;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DrawDiagram {

    /* drawDiagram method (first version)
     * export substations in svg files (substationId + ".svg")
     * instead, use methods from SingleDiagramTool class
     */
    public static void drawDiagram(Network network) {
        ComponentLibrary componentLibrary = new ResourcesComponentLibrary("Convergence", "/ConvergenceLibrary");

        LayoutParameters layoutParameters = new LayoutParameters();

        List<Substation> substations = new ArrayList<>();
        network.getSubstations().forEach(substations::add);

        int subNum = network.getSubstationCount();

        for (int i = 0; i < subNum; ++i) {
            SubstationDiagram substationDiagram = SubstationDiagram.build(new NetworkGraphBuilder(network), substations.get(i).getId());

            substationDiagram.writeSvg(
                "",
                componentLibrary,
                layoutParameters,
                new DefaultDiagramLabelProvider(network, componentLibrary, layoutParameters),
                new DefaultDiagramStyleProvider(),
                Paths.get("./svg/sub/" + substations.get(i).getId() + ".svg")
            );
        }
    }
}
