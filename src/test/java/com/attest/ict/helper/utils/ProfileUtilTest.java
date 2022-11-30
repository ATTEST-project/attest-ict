package com.attest.ict.helper.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ProfileUtilTest {

    private String fileNameWithUnderScore = "KPC_35_A_Bd.csv";
    private String fileNameYear = "Network1_W_Su_2030.xlsx";
    private String fileName = "Network1_Su_Sa.csv";

    // -- KPC_35_A_Bd.csv
    @Test
    void testGetNetworkNameWithUnderScore() {
        String fileName = fileNameWithUnderScore;
        String expected = "KPC_35";

        String[] loadProfile = ProfileUtil.getProfile(fileName);
        String networkName = ProfileUtil.getNetWorkName(loadProfile);

        assertThat(networkName).isEqualTo(expected);
    }

    @Test
    void testGetSeasonWithUnderScore() {
        String fileName = fileNameWithUnderScore;
        String expected = "A";

        String[] loadProfile = ProfileUtil.getProfile(fileName);
        String season = ProfileUtil.getSeason(loadProfile);

        assertThat(season).isEqualTo(expected);
    }

    @Test
    void testGetTipicalDayWithUnderScore() {
        String fileName = fileNameWithUnderScore;
        String expected = "Bd";

        String[] loadProfile = ProfileUtil.getProfile(fileName);
        String day = ProfileUtil.getTypicalDay(loadProfile);

        assertThat(day).isEqualTo(expected);
    }

    // -- Network1_W_Su_2030.xlsx
    @Test
    void testGetNetworkNameWithYear() {
        String fileName = fileNameYear;

        String expected = "Network1";

        String[] loadProfile = ProfileUtil.getProfile(fileName);
        String networkName = ProfileUtil.getNetWorkName(loadProfile);

        assertThat(networkName).isEqualTo(expected);
    }

    @Test
    void testGetSeasonWithYear() {
        String fileName = fileNameYear;
        String expected = "W";

        String[] loadProfile = ProfileUtil.getProfile(fileName);
        String season = ProfileUtil.getSeason(loadProfile);

        assertThat(season).isEqualTo(expected);
    }

    @Test
    void testGetTipicalDayWithYear() {
        String fileName = fileNameYear;
        String expected = "Su";

        String[] loadProfile = ProfileUtil.getProfile(fileName);
        String day = ProfileUtil.getTypicalDay(loadProfile);

        assertThat(day).isEqualTo(expected);
    }

    // -- Network1_Su_Sa.csv
    @Test
    void testGetNetworkName() {
        String expected = "Network1";

        String[] loadProfile = ProfileUtil.getProfile(fileName);
        String networkName = ProfileUtil.getNetWorkName(loadProfile);

        assertThat(networkName).isEqualTo(expected);
    }

    @Test
    void testGetSeason() {
        String expected = "Su";

        String[] loadProfile = ProfileUtil.getProfile(fileName);
        String season = ProfileUtil.getSeason(loadProfile);

        assertThat(season).isEqualTo(expected);
    }

    @Test
    void testGetTipicalDay() {
        String expected = "Sa";

        String[] loadProfile = ProfileUtil.getProfile(fileName);
        String day = ProfileUtil.getTypicalDay(loadProfile);

        assertThat(day).isEqualTo(expected);
    }
}
