package com.attest.ict.helper.matpower.flexibility.importer;

import com.attest.ict.custom.model.matpower.MatpowerFlexibilityModel;
import com.attest.ict.domain.FlexElVal;
import com.attest.ict.domain.FlexProfile;
import com.attest.ict.domain.Network;
import com.attest.ict.repository.NetworkRepository;
import java.util.*;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MatpowerFlexibilityImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatpowerFlexibilityImporter.class);

    static NetworkRepository networkRepository;

    // to call repository in a static function
    @Autowired
    NetworkRepository networkRepository1;

    @PostConstruct
    private void initStaticNetworkRepository() {
        networkRepository = this.networkRepository1;
    }

    private MatpowerFlexibilityImporter() {}

    private static Network findNetwork(String networkName, Long networkId) {
        Network network = null;
        if (networkId == null) {
            network = networkRepository.findByName(networkName).get();
        } else {
            network = networkRepository.findById(networkId).get();
        }
        return network;
    }

    public static FlexProfile createFlexProfile(MatpowerFlexibilityModel model, String networkName, Long networkId) {
        Network network = findNetwork(networkName, networkId);

        FlexProfile flexProfile = new FlexProfile();
        flexProfile.setMode(model.getFlexProfile().getMode());
        flexProfile.setTimeInterval(model.getFlexProfile().getTimeInterval());
        flexProfile.setNetwork(network);

        return flexProfile;
    }

    public static List<FlexElVal> createFlexElValues(MatpowerFlexibilityModel model, FlexProfile flexProfile) {
        List<FlexElVal> flexElValues = new ArrayList<>();

        for (int i = 0; i < model.getFlexBuses().size(); ++i) {
            for (int j = 0; j < model.getFlexPfUp().get(i).size(); ++j) {
                Date time = new Date();

                FlexElVal flexElVal = new FlexElVal();
                flexElVal.setBusNum(model.getFlexBuses().get(i));
                flexElVal.setHour(j);
                flexElVal.setMin(0);
                flexElVal.setPfmaxUp(model.getFlexPfUp().get(i).get(j));
                flexElVal.setPfmaxDn(model.getFlexPfDn().get(i).get(j));
                flexElVal.setQfmaxUp(model.getFlexQfUp().get(i).get(j));
                flexElVal.setQfmaxDn(model.getFlexQfDn().get(i).get(j));
                flexElVal.setFlexProfile(model.getFlexProfile());
                flexElValues.add(flexElVal);
            }
        }
        return flexElValues;
    }
}
