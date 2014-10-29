package com.nabijaczleweli.minecrasmer.compat.nei;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import com.nabijaczleweli.minecrasmer.util.JavaUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import java.util.*;

/**
 * In java, because scala has probjems overriding `Object...` construct
 * This is based on IC2's NEICraftingInWorldManager
 */
public class NEIMineCrASMerInWorldCraftingManager implements ICraftingHandler, IUsageHandler {
	private final Map<ItemStackWrapper, String> details = new TreeMap<ItemStackWrapper, String>();
	private final List<ItemStackWrapper>        offsets = new ArrayList<ItemStackWrapper>();
	private final List<PositionedStack>         outputs = new ArrayList<PositionedStack>();
	ItemStack target;


	@Override
	public IUsageHandler getUsageHandler(String inputId, Object... ingredients) {
		return this;
	}

	@Override
	public ICraftingHandler getRecipeHandler(String outputId, Object... results) {
		if(results.length > 0 && results[0] instanceof ItemStack) {
			NEIMineCrASMerInWorldCraftingManager inst = new NEIMineCrASMerInWorldCraftingManager();
			inst.target = (ItemStack)results[0];
			inst.addRecipes();
			return inst;
		} else
			return this;
	}

	@Override
	public boolean keyTyped(GuiRecipe gui, char keyChar, int keyCode, int recipe) {
		return false;
	}

	@Override
	public List<PositionedStack> getOtherStacks(int recipetype) {
		return new ArrayList<PositionedStack>();
	}

	@Override
	public PositionedStack getResultStack(int recipe) {
		return outputs.get(recipe);
	}

	@Override
	public void onUpdate() {}

	@Override
	public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe) {
		return currenttip;
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}

	@Override
	public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe) {
		return null;
	}

	@Override
	public int numRecipes() {
		return offsets.size();
	}

	@Override
	public boolean mouseClicked(GuiRecipe gui, int button, int recipe) {
		return false;
	}

	@Override
	public void drawBackground(int recipe) {
		GL11.glColor4f(1, 1, 1, 1);
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("hud.minecrasmer:compat.nei.inworld.label.name");
	}

	@Override
	public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe) {
		return null;
	}

	@Override
	public List<PositionedStack> getIngredientStacks(int recipe) {
		return new ArrayList<PositionedStack>();
	}

	@Override
	public void drawForeground(int recipe) {
		if(outputs.size() > recipe)
			Minecraft.getMinecraft().fontRenderer.drawSplitString(details.get(offsets.get(recipe)), 10, 25, 150, 0);
	}

	@Override
	public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
		return false;
	}

	@Override
	public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe) {
		return currenttip;
	}

	private void addRecipe(ItemStackWrapper wrapper, String msg) {
		ItemStack newStack = wrapper.is.copy();
		newStack.stackSize = 1;
		if(NEIServerUtils.areStacksSameTypeCrafting(newStack, target)) {
			offsets.add(wrapper);
			outputs.add(new PositionedStack(newStack, 75, 4));
			details.put(wrapper, msg);
		}
	}

	// Uses reflection because gradle compiles java BEFORE scala
	private void addRecipes() {
		try {
			Object NEICompatModule = JavaUtils.getModuleFromClass("compat.nei.NEI$");
			Item quartzModule = (Item)JavaUtils.getModuleFromClass("item.ItemQuartz$");

			int cleanQuartzShardsDamage = ((Number)JavaUtils.getValFromModule(quartzModule, "cleanShardsDamage")).intValue();
			String pureQuartzShardsDescription = (String)JavaUtils.getValFromModule(NEICompatModule, "getCleanQuartzShardsDescription");

			int quartzShardsDamage = ((Number)JavaUtils.getValFromModule(quartzModule, "shardsDamage")).intValue();
			String quartzShardsDescription = (String)JavaUtils.getValFromModule(NEICompatModule, "getQuartzShardsDescription");

			addRecipe(new ItemStackWrapper(new ItemStack(quartzModule, 1, cleanQuartzShardsDamage)), pureQuartzShardsDescription);
			addRecipe(new ItemStackWrapper(new ItemStack(quartzModule, 1, quartzShardsDamage)), quartzShardsDescription);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	private class ItemStackWrapper implements Comparable<ItemStackWrapper> {
		public ItemStackWrapper(ItemStack _is) {
			is = _is;
		}
		public ItemStack is;

		@Override
		public boolean equals(Object obj) {
			return obj != null && (obj == this || obj instanceof ItemStackWrapper && ItemStack.areItemStacksEqual(is, ((ItemStackWrapper)obj).is));
		}

		// This is pretty naive
		@Override
		public int compareTo(ItemStackWrapper o) {
			if(equals(o))
				return 0;
			if(o.is == null && is != null)
				return 1;
			return -1;
		}
	}
}
