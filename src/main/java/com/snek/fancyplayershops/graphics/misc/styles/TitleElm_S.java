package com.snek.fancyplayershops.graphics.misc.styles;

import com.snek.frameworklib.data_types.ui.TextOverflowBehaviour;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;




public class TitleElm_S extends SimpleTextElmStyle {
    public TitleElm_S() {
        super();
    }


    @Override
    public TextOverflowBehaviour getDefaultTextOverflowBehaviour() {
        return TextOverflowBehaviour.SCROLL;
    }
}
