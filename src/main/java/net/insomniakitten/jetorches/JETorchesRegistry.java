package net.insomniakitten.jetorches;

import net.insomniakitten.jetorches.block.BlockLamp;
import net.insomniakitten.jetorches.block.BlockTorch;
import net.insomniakitten.jetorches.color.ColoredLight;
import net.insomniakitten.jetorches.data.LampVariant;
import net.insomniakitten.jetorches.data.ItemVariant;
import net.insomniakitten.jetorches.data.TorchVariant;
import net.insomniakitten.jetorches.item.ItemLamp;
import net.insomniakitten.jetorches.item.ItemMaterial;
import net.insomniakitten.jetorches.item.ItemTorch;
import net.insomniakitten.jetorches.util.ModelSupplier;
import net.insomniakitten.jetorches.util.OreNameSupplier;
import net.insomniakitten.jetorches.util.RegistryCollection;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

@Mod.EventBusSubscriber(modid = JETorches.ID)
public final class JETorchesRegistry {

    private static final RegistryCollection<Block> TORCHES = new RegistryCollection<>();
    private static final RegistryCollection<Block> LAMPS = new RegistryCollection<>();

    private static final RegistryCollection<Item> ITEMS = new RegistryCollection<>();

    private JETorchesRegistry() {}

    @SubscribeEvent
    protected static void onBlockRegistry(RegistryEvent.Register<Block> event) {
        GameRegistry.registerTileEntity(ColoredLight.class, ColoredLight.ID);
        RegistryCollection<Block>.Registry torches = TORCHES.begin(event);
        for (TorchVariant torch : TorchVariant.VALUES) {
            torches.register(new BlockTorch(torch));
        }
        RegistryCollection<Block>.Registry lamps = LAMPS.begin(event);
        for (LampVariant lamp : LampVariant.VALUES) {
            lamps.register(new BlockLamp(lamp));
        }
    }

    @SubscribeEvent
    protected static void onItemRegistry(RegistryEvent.Register<Item> event) {
        RegistryCollection<Item>.Registry items = ITEMS.begin(event);
        for (ItemVariant material : ItemVariant.VALUES) {
            items.register(new ItemMaterial(material));
        }
        for (Block block : TORCHES.entries()) {
            items.register(new ItemTorch((BlockTorch) block));
        }
        for (Block block : LAMPS.entries()) {
            items.register(new ItemLamp((BlockLamp) block));
        }
        for (Item item : ITEMS.entries()) {
            if (item instanceof OreNameSupplier) {
                for (String name : ((OreNameSupplier) item).getOreNames()) {
                    OreDictionary.registerOre(name, item);
                }
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    protected static void onModelRegistry(ModelRegistryEvent event) {
        for (Item item : ITEMS.entries()) {
            if (item instanceof ModelSupplier) {
                ModelResourceLocation mrl = ((ModelSupplier) item).getModelResourceLocation();
                ModelLoader.setCustomModelResourceLocation(item, 0, mrl);
            }
        }
    }

}
