package com.attest.ict.helper.matpower.common.util.structure;

import static com.attest.ict.helper.matpower.flexibility.util.MatpowerFlexibilitySection.*;
import static com.attest.ict.helper.matpower.network.util.MatpowerNetworkSection.*;

import com.attest.ict.helper.matpower.common.util.MatpowerSection;
import java.util.ArrayList;
import java.util.List;

public class MpcBaseElement {

    private final MatpowerSection section;
    private final String mpc;
    private List<String> comments;
    private String simpleContent = "";

    public MpcBaseElement(MatpowerSection section) {
        this.section = section;
        this.mpc = "mpc." + section.getLabel();
        this.comments = setInitialComments(section);
    }

    private List<String> setInitialComments(MatpowerSection section) {
        List<String> comments = new ArrayList<>();
        if (VERSION.equals(section)) {
            comments.add("%% MATPOWER Case Format : Version 2");
        } else if (BASE_MVA.equals(section)) {
            comments.add("%%-----  Power Flow Data  -----%%");
            comments.add("%% system MVA base");
        } else if (V_LEVELS.equals(section)) {
            comments.add("%% system voltage levels (kV)");
        } else if (BUS.equals(section)) {
            comments.add("%% bus data");
            comments.add("% bus_i\ttype\tPd\tQd\tGs\tBs\tarea\tVm\tVa\tbaseKV\tzone\tVmax\tVmin");
        } else if (BRANCH.equals(section)) {
            comments.add("% branch data");
            comments.add("% fbus\ttbus\tr\tx\tb\t\trateA\trateB\trateC\tratio\tangle\tstatus\tangmin\tangmax");
        } else if (GENERATOR.equals(section)) {
            comments.add("%% generator data");
            comments.add(
                "% bus\tPg\tQg\tQmax\tQmin\tVg\tmBase\tstatus\tPmax\tPmin\tPc1\tPc2\tQc1min\tQc1max\tQc2min\tQc2max\tramp_acg\tramp_10\tramp_30\tramp_q\tApf"
            );
        } else if (CAP_BANK_DATA.equals(section)) {
            comments.add("%% CAPACITOR BANK Data");
            comments.add("%\tbus_i\tNode_ID\tbank_ID\tQnom (Mvar)");
        } else if (BUS_COORDINATES.equals(section)) {
            comments.add("");
            comments.add("");
        } else if (BUS_NAMES.equals(section)) {
            comments.add("%% bus names");
        } else if (GEN_TAGS.equals(section)) {
            comments.add(
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
                "%  OTH (Other thermal, such as biomass, biogas, Municipal solid waste and CHP renewable and non-renewable)\n"
            );
            comments.add("%\tgenType");
        } else if (TRANSFORMER.equals(section)) {
            comments.add("% transformers data");
            comments.add("% from bus\tto bus\tmin\tmax\ttotaltaps\ttap");
        } else if (FLEXBUS.equals(section)) {
            comments.add("%% flexibility connection data");
        } else if (FLEXPROF.equals(section)) {
            comments.add("%% flexibility profile mode");
            comments.add("% Mode \t\t\t\t\t\t\t\t\t\t\t\t% time interval:");
            comments.add("% 3 = representative business day for a season\t\t%Fraction of an hour, e.g., 1 = 1h, 0.5 = 30 mins");
        } else if (PF_UP.equals(section)) {
            comments.add("%% Active upwards flexibility data");
            comments.add("%   time (hourly)");
            comments.add("% 1\t2\t3\t…\t23\t24\t\t% hour");
        } else if (PF_DN.equals(section)) {
            comments.add("%% Active downwards flexibility data");
            comments.add("%   time (hourly)");
            comments.add("% 1\t2\t3\t…\t23\t24\t\t% hour");
        } else if (QF_UP.equals(section)) {
            comments.add("%% Reactive upwards flexibility data");
            comments.add("%   time (hourly)");
            comments.add("% 1\t2\t3\t…\t23\t24\t\t% hour");
        } else if (QF_DN.equals(section)) {
            comments.add("%% Reactive downwards flexibility data");
            comments.add("%   time (hourly)");
            comments.add("% 1\t2\t3\t…\t23\t24\t\t% hour");
        } else if (GEN_COST.equals(section)) {
            comments.add("%%-----  OPF Data  -----%%");
            comments.add("%% generator cost data	");
            comments.add("%\t1\tstartup\tshutdown\tn\tx1\ty1\t...\txn\tyn");
            comments.add("%\t2\tstartup\tshutdown\tn\tc(n-1)\t...\tc0");
        } else {
            comments.add("");
        }
        return comments;
    }

    public MatpowerSection getSection() {
        return section;
    }

    public String getMpc() {
        return mpc;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public String getSimpleContent() {
        return simpleContent;
    }

    public void setSimpleContent(String simpleContent) {
        this.simpleContent = simpleContent;
    }

    protected String getCommentsFormatted() {
        return String.join("\n", comments);
    }

    public String getMpcElement() {
        return getCommentsFormatted() + "\n" + mpc + " = " + simpleContent + ";\n";
    }

    @Override
    public String toString() {
        return getMpcElement();
    }
}
