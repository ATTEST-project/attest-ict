package com.attest.ict.helper.matpower.network.annotated;

import com.attest.ict.domain.GenTag;
import com.univocity.parsers.annotations.Parsed;

public class GenTagAnnotated extends GenTag {

    @Parsed(index = 0)
    public String getGenTag() {
        return super.getGenTag();
    }

    @Parsed(index = 0, field = "gen_tag")
    public void setGenTag(String genTag) {
        super.setGenTag(genTag);
    }
}
