package cn.mmf.slashblade_tic.client.book;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.client.book.data.content.PageContent;
import slimeknights.tconstruct.library.book.sectiontransformer.AbstractMaterialSectionTransformer;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;

@SideOnly(Side.CLIENT)
public class BladeMaterialSectionTransformer extends AbstractMaterialSectionTransformer {

    public BladeMaterialSectionTransformer() {
        super("materials");
    }

    @Override
    protected boolean isValidMaterial(Material material) {
      return material.hasStats(MaterialTypes.HEAD) || material.hasStats(MaterialTypes.HEAD) || material.hasStats(MaterialTypes.HEAD);
    }

    @Override
    protected PageContent getPageContent(Material material) {
      return new ContentBladeMaterial(material);
    }
}
