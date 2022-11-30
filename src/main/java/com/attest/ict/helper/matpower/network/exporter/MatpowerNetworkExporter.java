package com.attest.ict.helper.matpower.network.exporter;

import com.attest.ict.custom.model.matpower.MatpowerModel;
import com.attest.ict.helper.matpower.common.util.structure.MatpowerFileStruct;
import com.attest.ict.helper.matpower.network.writer.MatpowerNetworkWriter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class MatpowerNetworkExporter {

    private MatpowerNetworkExporter() {}

    public static InputStream exportMatpowerData(MatpowerModel model) {
        MatpowerFileStruct struct = MatpowerNetworkWriter.generateMatpowerStructure(model);
        return new ByteArrayInputStream(struct.toString().getBytes());
    }
}
