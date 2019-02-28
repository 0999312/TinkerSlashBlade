package cn.mmf.slashblade_tic.client.book;

import cn.mmf.slashblade_tic.Main;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.client.book.BookLoader;
import slimeknights.mantle.client.book.BookTransformer;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.repository.FileRepository;

@SideOnly(Side.CLIENT)
public class BookBladeSmith extends BookData {
	public final static BookData INSTANCE = BookLoader.registerBook(Main.MODID, false, false);

    public static void init() {
        BookLoader.registerPageType(ContentBladeMaterial.ID, ContentBladeMaterial.class);
        BookLoader.registerPageType(ContentBladeModifier.ID, ContentBladeModifier.class);
        BookLoader.registerPageType(ContentBlade.ID, ContentBlade.class);
        INSTANCE.addRepository(new FileRepository(new ResourceLocation(Main.MODID, "book").toString()));
        INSTANCE.addTransformer(new BladeMaterialSectionTransformer());
        INSTANCE.addTransformer(new BladeModifierSectionTransformer());
        INSTANCE.addTransformer(new BladeAccessorySectionTransformer());
        INSTANCE.addTransformer(BookTransformer.IndexTranformer());
    }
}
