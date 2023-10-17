package com.attest.ict.helper.matpower.network.reader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.attest.ict.custom.model.matpower.MatpowerModel;
import com.attest.ict.domain.GenCost;
import com.attest.ict.domain.Generator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MatpowerNetworkReaderTest {

    private final String filePath = "src\\test\\resources\\m_file\\case5.m";

    @Test
    void readGen() {
        int expectedGen = 4;
        File fileToParse = new File(filePath);
        FileInputStream input;
        try {
            input = new FileInputStream(fileToParse);
            MatpowerModel matpowerModel = MatpowerNetworkReader.read(input);
            int numGen = matpowerModel.getGenerators().size();
            assertEquals(expectedGen, numGen);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            assertThat(Boolean.FALSE).isEqualTo(Boolean.TRUE);
        }
    }

    @Test
    void read() {
        int expectedGenCost = 4;
        File fileToParse = new File(filePath);
        FileInputStream input;
        try {
            input = new FileInputStream(fileToParse);
            MatpowerModel matpowerModel = MatpowerNetworkReader.read(input);

            List<GenCost> genCostList = matpowerModel.getGenCosts();
            System.out.println(Arrays.toString(genCostList.toArray()));
            for (int i = 0; i < genCostList.size(); i++) {
                GenCost genCost = genCostList.get(i);
                Integer model = genCost.getModel();
                Double startUp = genCost.getStartup();
                Double shutdown = genCost.getShutdown();
                Long n = genCost.getnCost();
                assertThat(model.equals(2));
                assertThat(n.equals(2));
                String costPf = genCost.getCostPF();
                System.out.println(genCost + " index " + i);
                switch (i) {
                    case 0:
                        assertEquals("10|0|0", costPf);
                        break;
                    case 1:
                        assertEquals("15|0|0", costPf);
                        break;
                    case 2:
                        assertEquals("20|0|0", costPf);
                        break;
                    case 3:
                        assertEquals("30|0|0", costPf);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            assertThat(Boolean.FALSE).isEqualTo(Boolean.TRUE);
        }
    }

    @DisplayName("Case 5 Only Active Power parameter cost")
    @Test
    void case5ReadGenCosts() {
        String filePathCostPf = "src\\test\\resources\\m_file\\case5.m";
        System.out.println("Running test: case5ReadGenCosts");
        File fileToParse = new File(filePathCostPf);
        FileInputStream input;
        try {
            input = new FileInputStream(fileToParse);
            MatpowerModel matpowerModel = MatpowerNetworkReader.read(input);
            List<Generator> generatorList = matpowerModel.getGenerators();
            List<GenCost> genCosts = matpowerModel.getGenCosts();
            assertEquals(generatorList.size(), genCosts.size());
            for (int i = 0; i < genCosts.size(); i++) {
                GenCost genCost = genCosts.get(i);
                String costPf = genCost.getCostPF();
                String costQf = genCost.getCostQF();
                System.out.println(genCost + " index " + i);
                switch (i) {
                    case 0:
                        assertAll("Row 0 succesfully ", () -> assertEquals("10|0|0", costPf), () -> assertNull(costQf));
                        System.out.println("Cost for gen 1 is correct!");
                        break;
                    case 1:
                        assertAll("Row 1 succesfully ", () -> assertEquals("15|0|0", costPf), () -> assertNull(costQf));
                        System.out.println("Cost for gen 2 is correct!");
                        break;
                    case 2:
                        assertAll("Row 2 succesfully ", () -> assertEquals("20|0|0", costPf), () -> assertNull(costQf));
                        System.out.println("Cost for gen 3 is correct!");
                        break;
                    case 3:
                        assertAll("Row 3 succesfully ", () -> assertEquals("30|0|0", costPf), () -> assertNull(costQf));
                        System.out.println("Cost for gen 4 is correct!");
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error running test: case5ReadGenCosts " + e.getMessage());
            assertThat(Boolean.FALSE).isEqualTo(Boolean.TRUE);
        }
        System.out.println("End test: case5ReadGenCosts");
    }

    @Test
    void case5CostPCostFGenCosts() {
        String filePathCostQf = "src\\test\\resources\\m_file\\case5_test.m";
        System.out.println("Running test: case5CostPCostFGenCosts");
        File fileToParse = new File(filePathCostQf);
        FileInputStream input;
        try {
            input = new FileInputStream(fileToParse);
            MatpowerModel matpowerModel = MatpowerNetworkReader.read(input);
            List<GenCost> genCosts = matpowerModel.getGenCosts();
            System.out.println(Arrays.toString(genCosts.toArray()));
            for (int i = 0; i < genCosts.size(); i++) {
                GenCost genCost = genCosts.get(i);
                String costPf = genCost.getCostPF();
                String costQf = genCost.getCostQF();
                System.out.println(genCost + " index " + i);
                switch (i) {
                    case 0:
                        assertAll("Row 0 succesfully ", () -> assertEquals("10|0|0", costPf), () -> assertEquals(null, costQf));
                        System.out.println("Cost for gen 1 is correct!");
                        break;
                    case 1:
                        assertAll("Row 1 succesfully ", () -> assertEquals("15|0|0", costPf), () -> assertEquals(null, costQf));
                        System.out.println("Cost for gen 2 is correct!");
                        break;
                    case 2:
                        assertAll("Row 2 succesfully ", () -> assertEquals("20|0|0", costPf), () -> assertEquals(null, costQf));
                        System.out.println("Cost for gen 3 is correct!");
                        break;
                    case 3:
                        assertAll("Row 3 succesfully ", () -> assertEquals("30|0|0", costPf), () -> assertEquals(null, costQf));
                        System.out.println("Cost for gen 4 is correct!");
                        break;
                    case 4:
                        assertAll("Row 4 succesfully ", () -> assertEquals("24|0|0", costPf), () -> assertEquals(null, costQf));
                        System.out.println("Cost for gen 1 is correct!");
                        break;
                    case 5:
                        assertAll("Row 1 succesfully ", () -> assertEquals("36|0|0", costPf), () -> assertEquals(null, costQf));
                        System.out.println("Cost for gen 2 is correct!");
                        break;
                    case 6:
                        assertAll("Row 5 succesfully ", () -> assertEquals("41|0|0", costPf), () -> assertEquals(null, costQf));
                        System.out.println("Cost for gen 3 is correct!");
                        break;
                    case 7:
                        assertAll("Row 6 succesfully ", () -> assertEquals("12|0|0", costPf), () -> assertEquals(null, costQf));
                        System.out.println("Cost for gen 4 is correct!");
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error running test: case5CostPCostFGenCosts " + e.getMessage());
            assertThat(Boolean.FALSE).isEqualTo(Boolean.TRUE);
        }
        System.out.println("End test: case5CostPCostFGenCosts");
    }

    @Test
    void caseUKTXReadGenCosts() {
        String filePathCostQf = "src\\test\\resources\\m_file\\Transmission_Network_UK.m";
        System.out.println("Running test: caseUKTXReadGenCosts");
        File fileToParse = new File(filePathCostQf);
        FileInputStream input;
        try {
            input = new FileInputStream(fileToParse);
            MatpowerModel matpowerModel = MatpowerNetworkReader.read(input);
            List<Generator> generatorList = matpowerModel.getGenerators();
            List<GenCost> genCosts = matpowerModel.getGenCosts();
            for (int i = 0; i < genCosts.size(); i++) {
                GenCost genCost = genCosts.get(i);
                String costPf = genCost.getCostPF();
                String costQf = genCost.getCostQF();
                System.out.println(genCost + " index " + i);
                switch (i) {
                    case 2:
                        assertAll("Row 2 succesfully ", () -> assertEquals("70|0", costPf), () -> assertNull(costQf));
                        System.out.println("Cost for gen 2 is correct!");
                        break;
                    case 5:
                        assertAll("Row 5 succesfully ", () -> assertEquals("38.05|0", costPf), () -> assertNull(costQf));
                        System.out.println("Cost for gen 5 is correct!");
                        break;
                    case 99:
                        assertAll("Row 99 succesfully ", () -> assertEquals("8.05|0", costPf), () -> assertNull(costQf));
                        System.out.println("Cost for gen 99 is correct!");
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error running test: caseUKTXReadGenCosts " + e.getMessage());
            assertThat(Boolean.FALSE).isEqualTo(Boolean.TRUE);
        }
        System.out.println("End test: caseUKTXReadGenCosts");
    }

    @Test
    void casePT1ReadGenCosts() {
        String filePathCostQf = "src\\test\\resources\\m_file\\Distribution_Network_PT1_new.m";
        System.out.println("Running test: casePT1ReadGenCosts");
        File fileToParse = new File(filePathCostQf);
        FileInputStream input;
        try {
            input = new FileInputStream(fileToParse);
            MatpowerModel matpowerModel = MatpowerNetworkReader.read(input);
            List<Generator> generatorList = matpowerModel.getGenerators();
            List<GenCost> genCosts = matpowerModel.getGenCosts();
            for (int i = 0; i < genCosts.size(); i++) {
                GenCost genCost = genCosts.get(i);
                Long nCost = genCost.getnCost();
                String costPf = genCost.getCostPF();
                String costQf = genCost.getCostQF();
                System.out.println(genCost + " index " + i);
                if (i == 0) {
                    assertAll(
                        "Row 0 succesfully ",
                        () -> assertEquals(2, genCost.getModel()),
                        () -> assertEquals(0.0, genCost.getStartup()),
                        () -> assertEquals(0.0, genCost.getShutdown()),
                        () -> assertEquals(3, nCost),
                        () -> assertEquals("0|0|0.00", costPf),
                        () -> assertNull(costQf)
                    );
                    System.out.println("Cost for gen 1 is correct!");
                }
            }
        } catch (IOException e) {
            System.out.println("Error running test: casePT1ReadGenCosts " + e.getMessage());
            assertThat(Boolean.FALSE).isEqualTo(Boolean.TRUE);
        }
        System.out.println("End test: casePT1ReadGenCosts");
    }

    @Test
    void caseDxUkUrbanReadGenCosts() {
        String filePathCostQf = "src\\test\\resources\\m_file\\Distribution_Network_Urban_UK_new.m";
        System.out.println("Running test: caseDxUkUrbanReadGenCosts");
        File fileToParse = new File(filePathCostQf);
        FileInputStream input;
        try {
            input = new FileInputStream(fileToParse);
            MatpowerModel matpowerModel = MatpowerNetworkReader.read(input);
            List<Generator> generatorList = matpowerModel.getGenerators();
            List<GenCost> genCosts = matpowerModel.getGenCosts();
            for (int i = 0; i < genCosts.size(); i++) {
                GenCost genCost = genCosts.get(i);
                Long nCost = genCost.getnCost();
                String costPf = genCost.getCostPF();
                String costQf = genCost.getCostQF();
                System.out.println(genCost + " index " + i);
                if (i == 0) {
                    assertAll(
                        "Row 0 succesfully ",
                        () -> assertEquals(2, genCost.getModel()),
                        () -> assertEquals(0, genCost.getStartup()),
                        () -> assertEquals(0, genCost.getShutdown()),
                        () -> assertEquals(2, nCost),
                        () -> assertEquals("0|0", costPf),
                        () -> assertNull(costQf)
                    );
                    System.out.println("Cost for gen 1 is correct!");
                }
            }
        } catch (IOException e) {
            System.out.println("Error running test: caseDxUkUrbanReadGenCosts " + e.getMessage());
            assertThat(Boolean.FALSE).isEqualTo(Boolean.TRUE);
        }
        System.out.println("End test: caseDxUkUrbanReadGenCosts");
    }

    @Test
    void caseES03ReadGen() {
        String filePath = "src\\test\\resources\\m_file\\matpower_rnm_network.m";
        System.out.println("Running test: caseES3CreateGen");
        File fileToParse = new File(filePath);
        FileInputStream input;
        try {
            input = new FileInputStream(fileToParse);
            MatpowerModel matpowerModel = MatpowerNetworkReader.read(input);
            List<Generator> gens = matpowerModel.getGenerators();
            System.out.println("Generator found {}  " + gens.size());
            for (int i = 0; i < gens.size(); i++) {
                Generator gen = gens.get(i);
                System.out.println("Generator  " + gen);
                assertAll(
                    "Row 0 succesfully ",
                    () -> assertEquals(1, gen.getBusNum()),
                    () -> assertEquals(1000.0, gen.getPmax()),
                    () -> assertEquals(0.0, gen.getPmin())
                );
                System.out.println("Gen for gen 1 is correct!");
            }
        } catch (IOException e) {
            System.out.println("Error running test: caseES03ReadGen " + e.getMessage());
            assertThat(Boolean.FALSE).isEqualTo(Boolean.TRUE);
        }
        System.out.println("End test: caseES03ReadGen");
    }
}
