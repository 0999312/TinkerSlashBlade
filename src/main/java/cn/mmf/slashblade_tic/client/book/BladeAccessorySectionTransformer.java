package cn.mmf.slashblade_tic.client.book;

import cn.mmf.slashblade_tic.blade.TinkerSlashBladeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.PageData;
import slimeknights.tconstruct.library.book.content.ContentListing;
import slimeknights.tconstruct.library.book.sectiontransformer.ContentListingSectionTransformer;
import slimeknights.tconstruct.library.modifiers.IModifier;

@SideOnly(Side.CLIENT)
public class BladeAccessorySectionTransformer extends ContentListingSectionTransformer {

    public BladeAccessorySectionTransformer() {
        super("accessories");
    }

    @Override
    protected void processPage(BookData book, ContentListing listing, PageData page) {
        if(page.content instanceof ContentBladeModifier) {
            IModifier modifier = TinkerSlashBladeRegistry.getModifier(((ContentBladeModifier) page.content).modifierName);
            if(modifier != null) {
                listing.addEntry(modifier.getLocalizedName(), page);
            }
        }
    }
}
