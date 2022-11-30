package com.attest.ict.helper.matpower.common.util.structure;

import com.attest.ict.helper.matpower.common.util.MatpowerSection;
import com.attest.ict.helper.matpower.network.util.MatpowerNetworkSection;
import java.util.ArrayList;
import java.util.List;

public class MpcElement extends MpcBaseElement {

    private List<String> content = new ArrayList<>();

    public MpcElement(MatpowerSection section) {
        super(section);
    }

    @Override
    public String getMpcElement() {
        if (!content.isEmpty()) {
            return super.getCommentsFormatted() + "\n" + super.getMpc() + " = [\n" + getContentFormatted() + "];\n";
        }
        return super.getMpc();
    }

    private String getContentFormatted() {
        return String.join("", content);
    }

    public MpcElement setContent(List<String> content) {
        this.content.clear();
        this.content = content;
        return this;
    }

    public MpcElement addContent(List<String> data) {
        content.addAll(data);
        return this;
    }

    public List<String> getContent() {
        return content;
    }

    @Override
    public String toString() {
        return getMpcElement();
    }
}
