package com.attest.ict.custom.diagrams.layout;

import com.powsybl.sld.layout.ForceSubstationLayoutFactory;
import com.powsybl.sld.layout.SubstationLayoutFactory;
import com.powsybl.sld.layout.VoltageLevelLayoutFactory;
import com.powsybl.sld.layout.ZoneLayoutFactory;
import com.powsybl.sld.model.ZoneGraph;

public class ForceZoneLayoutFactory implements ZoneLayoutFactory {

    private ForceSubstationLayoutFactory.CompactionType compactionType;

    public ForceZoneLayoutFactory(ForceSubstationLayoutFactory.CompactionType compactionType) {
        this.compactionType = compactionType;
    }

    @Override
    public ForceZoneLayout create(ZoneGraph graph, SubstationLayoutFactory sLayoutFactory, VoltageLevelLayoutFactory vLayoutFactory) {
        return new ForceZoneLayout(graph, sLayoutFactory, vLayoutFactory, compactionType);
    }
}
