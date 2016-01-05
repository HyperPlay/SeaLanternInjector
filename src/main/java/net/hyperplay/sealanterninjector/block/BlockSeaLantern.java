package net.hyperplay.sealanterninjector.block;

import net.minecraft.server.v1_7_R4.Block;
import net.minecraft.server.v1_7_R4.CreativeModeTab;

public class BlockSeaLantern extends Block {

    public BlockSeaLantern() {
        super(net.minecraft.server.v1_7_R4.Material.SHATTERABLE); //Shatters, like glass.
        this.a(CreativeModeTab.b); //Building Tab in Creative. Don't ask me why this is part of the server.
    }

    //You can override any Block methods to customize stuff such as drops. There are currently no drops.

}
