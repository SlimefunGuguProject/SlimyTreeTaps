package io.github.thebusybiscuit.slimytreetaps;

import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;

public class TreeTaps extends JavaPlugin implements SlimefunAddon {
	
	@Override
	public void onEnable() {
		Config cfg = new Config(this);
		
		if (cfg.getBoolean("options.auto-update") && getDescription().getVersion().startsWith("DEV - ")) {
			new GitHubBuildsUpdater(this, getFile(), "ybw0014/SlimyTreeTaps-CN/master").start();
		}
		
		new Metrics(this, 6138);
		
		SlimefunItemStack treeTap = new SlimefunItemStack("TREE_TAP", Material.WOODEN_HOE, "&6木龙头", getLore("树脂", cfg.getInt("resin-chance.standard")));
		SlimefunItemStack reinforcedTreeTap = new SlimefunItemStack("REINFORCED_TREE_TAP", Material.IRON_HOE, "&6强化木龙头", getLore("树脂", cfg.getInt("resin-chance.reinforced")));
		SlimefunItemStack diamondTreeTap = new SlimefunItemStack("DIAMOND_TREE_TAP", Material.DIAMOND_HOE, "&b钻石木龙头", getLore("树脂", cfg.getInt("resin-chance.diamond")));
		SlimefunItemStack treeScraper = new SlimefunItemStack("TREE_SCRAPER", Material.GOLDEN_SHOVEL, "&b树皮刮刀", getLore("琥珀", cfg.getInt("amber-chance")));

		clearAttributes(treeTap, reinforcedTreeTap, diamondTreeTap, treeScraper);
		
		SlimefunItemStack stickyResin = new SlimefunItemStack("STICKY_RESIN", Material.BROWN_DYE, "&6粘性树脂", "", "&7可以制成橡胶");
		SlimefunItemStack rubber = new SlimefunItemStack("RUBBER", Material.FIREWORK_STAR, "&e橡胶", "", "&7塑料的替代来源");
		SlimefunItemStack rawPlastic = new SlimefunItemStack("RAW_PLASTIC", Material.PAPER, "&f生塑料");
		
		SlimefunItemStack rubberFactory = new SlimefunItemStack("RUBBER_FACTORY", Material.SMOKER, "&b橡胶工厂", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &712 J/s");
		SlimefunItemStack resinExtractor = new SlimefunItemStack("RESIN_EXTRACTOR", Material.SMITHING_TABLE, "&c树脂提取器", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 1x", "&8\u21E8 &e\u26A1 &732 J/s");
		SlimefunItemStack resinExtractor2 = new SlimefunItemStack("RESIN_EXTRACTOR_2", Material.SMITHING_TABLE, "&c树脂提取器 &7(&eII&7)", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7速度: 2x", "&8\u21E8 &e\u26A1 &756 J/s");
		
		SlimefunItemStack amber = new SlimefunItemStack("AMBER", "ac7f7b72fc3e733828fcccc0ca8278aca2633aa33a231c93a682d14ac54aa0c4", "&6琥珀", "", "&e从树脂中提炼的硬化宝石");
		SlimefunItemStack amberBlock = new SlimefunItemStack("AMBER_BLOCK", Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_16) ? Material.SHROOMLIGHT: Material.GLOWSTONE, "&6琥珀块");
        
		SlimefunItemStack blueEnderPearl = new SlimefunItemStack("BLUE_ENDER_PEARL", "38be8abd66d09a58ce12d377544d726d25cad7e979e8c2481866be94d3b32f", "&9蓝色末影珍珠", "", "&7该物品可用于制作魔镜");
		SlimefunItemStack magicalMirror = new SlimefunItemStack("MAGICAL_MIRROR", Material.BLUE_STAINED_GLASS_PANE, "&9魔镜 &7(未绑定)", "", "&e右键点击 &7绑定此魔镜", "&7到当前位置", "", "&7将已绑定的魔镜放入", "&7物品展示框,并点击物品", "&7来传送到绑定的位置", "&7传送需要花费&b1个末影珍珠");
        
		ItemGroup itemGroup = new ItemGroup(new NamespacedKey(this, "tree_taps"), new CustomItemStack(treeTap, "&6粘液木龙头", "", "&a> 单击打开"));
		RecipeType rubberFactoryType = new RecipeType(new NamespacedKey(this, "rubber_factory"), rubberFactory);
		
		new TreeTool(itemGroup, treeTap, cfg.getInt("resin-chance.standard"), stickyResin,
		new ItemStack[] {
				null, SlimefunItems.DAMASCUS_STEEL_INGOT, new ItemStack(Material.OAK_LOG),
				SlimefunItems.DAMASCUS_STEEL_INGOT, new ItemStack(Material.OAK_LOG), null,
				new ItemStack(Material.OAK_LOG), null, new ItemStack(Material.BOWL)
		}).register(this);
		
		new TreeTool(itemGroup, reinforcedTreeTap, cfg.getInt("resin-chance.reinforced"), stickyResin,
		new ItemStack[] {
				null, SlimefunItems.HARDENED_METAL_INGOT, new ItemStack(Material.OAK_LOG),
				SlimefunItems.HARDENED_METAL_INGOT, treeTap, null,
				new ItemStack(Material.OAK_LOG), null, SlimefunItems.COBALT_INGOT
		}).register(this);
		
		new TreeTool(itemGroup, diamondTreeTap, cfg.getInt("resin-chance.diamond"), stickyResin,
		new ItemStack[] {
				null, new ItemStack(Material.DIAMOND), new ItemStack(Material.OAK_LOG),
				new ItemStack(Material.DIAMOND), reinforcedTreeTap, null,
				new ItemStack(Material.OAK_LOG), null, SlimefunItems.CARBONADO
		}).register(this);
        
        new TreeTool(itemGroup, treeScraper, cfg.getInt("amber-chance"), amber,
        new ItemStack[] {
                null, new ItemStack(Material.GOLD_INGOT), null,
                new ItemStack(Material.GOLD_INGOT), treeTap, null,
                null, null, SlimefunItems.BRONZE_INGOT
        }).register(this);
		
		new SlimefunItem(itemGroup, stickyResin, new RecipeType(new NamespacedKey(this, "tree_tap"), treeTap),
		new ItemStack[] {
				null, null, null,
				null, new ItemStack(Material.OAK_LOG), null,
				null, null, null
		}).register(this);
		
		new RubberFactory(itemGroup, rubberFactory, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {
				null, SlimefunItems.HEATING_COIL, null,
				SlimefunItems.SOLDER_INGOT, SlimefunItems.ELECTRIC_FURNACE_2, SlimefunItems.SOLDER_INGOT,
				SlimefunItems.SOLDER_INGOT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.SOLDER_INGOT
		}) {
			
			@Override
			public void registerDefaultRecipes() {
				registerRecipe(4, new ItemStack[] {new CustomItemStack(stickyResin, 2)}, new ItemStack[] {rubber});
				registerRecipe(6, new ItemStack[] {new CustomItemStack(rubber, 2)}, new ItemStack[] {rawPlastic});
				registerRecipe(10, new ItemStack[] {rawPlastic}, new ItemStack[] {SlimefunItems.PLASTIC_SHEET});
			}

			@Override
			public int getEnergyConsumption() {
				return 6;
			}

			@Override
			public int getSpeed() {
				return 1;
			}
			
		}.register(this);
		
		new ResinExtractor(itemGroup, resinExtractor, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {
				null, diamondTreeTap, null,
				SlimefunItems.GOLD_24K, SlimefunItems.CARBONADO, SlimefunItems.GOLD_24K,
				SlimefunItems.ELECTRIC_MOTOR, rubberFactory, SlimefunItems.ELECTRIC_MOTOR
		}) {
			
			@Override
			public void registerDefaultRecipes() {
				for (Material log : Tag.LOGS.getValues()) {
					if (!log.name().startsWith("STRIPPED_")) {
						registerRecipe(14, new ItemStack[] {new ItemStack(log, 8)}, new ItemStack[] {stickyResin});
					}
				}
			}

			@Override
			public int getEnergyConsumption() {
				return 16;
			}

			@Override
			public int getSpeed() {
				return 1;
			}

            @Override
			public int getCapacity() {
			    return 1024;
			}
			
		}.register(this);
		
		new ResinExtractor(itemGroup, resinExtractor2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {
				SlimefunItems.REINFORCED_ALLOY_INGOT, diamondTreeTap, SlimefunItems.REINFORCED_ALLOY_INGOT,
				SlimefunItems.GOLD_24K, SlimefunItems.CARBONADO, SlimefunItems.GOLD_24K,
				SlimefunItems.ELECTRIC_MOTOR, resinExtractor, SlimefunItems.ELECTRIC_MOTOR
		}) {
			
			@Override
			public void registerDefaultRecipes() {
				for (Material log : Tag.LOGS.getValues()) {
					if (!log.name().startsWith("STRIPPED_")) {
						registerRecipe(6, new ItemStack[] {new ItemStack(log, 8)}, new ItemStack[] {stickyResin});
					}
				}
			}

			@Override
			public int getEnergyConsumption() {
				return 28;
			}

			@Override
			public int getSpeed() {
				return 2;
			}

            @Override
            public int getCapacity() {
                return 2048;
            }
			
		}.register(this);
		
		new SlimefunItem(itemGroup, rawPlastic, rubberFactoryType,
		new ItemStack[] {
				null, null, null,
				null, new SlimefunItemStack(rubber, 2), null,
				null, null, null
		}).register(this);

		new SlimefunItem(itemGroup, rubber, rubberFactoryType,
		new ItemStack[] {
				null, null, null,
				null, stickyResin, null,
				null, null, null
		}).register(this);

        new SlimefunItem(itemGroup, amber, RecipeType.SMELTERY,
        new ItemStack[] {
                new SlimefunItemStack(stickyResin, 4), null, null,
                null, null, null,
                null, null, null
        }).register(this);

        new SlimefunItem(itemGroup, amberBlock, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {
                amber, amber, amber,
                amber, amber, amber,
                amber, amber, amber
        }).register(this);
        
        new UnplaceableBlock(itemGroup, blueEnderPearl, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {
                new ItemStack(Material.LAPIS_BLOCK), amberBlock, new ItemStack(Material.LAPIS_BLOCK),
                amberBlock, new ItemStack(Material.ENDER_PEARL), amberBlock,
                new ItemStack(Material.LAPIS_BLOCK), amberBlock, new ItemStack(Material.LAPIS_BLOCK)
        }).register(this);
        
        MagicalMirror mirror = new MagicalMirror(this, itemGroup, magicalMirror, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {
                new ItemStack(Material.GLASS), amber, new ItemStack(Material.GLASS),
                amber, blueEnderPearl, amber,
                new ItemStack(Material.GLASS), amber, new ItemStack(Material.GLASS)
        });
        mirror.register(this);

        Research treeTapsResearch = new Research(new NamespacedKey(this, "tree_taps"), 6789, "木龙头", 15);
		treeTapsResearch.addItems(treeTap, reinforcedTreeTap, diamondTreeTap, stickyResin, rubber, rawPlastic);
		treeTapsResearch.register();

		Research automationResearch = new Research(new NamespacedKey(this, "rubber_automation"), 6790, "自动化橡胶", 20);
		automationResearch.addItems(rubberFactory, resinExtractor, resinExtractor2);
		automationResearch.register();

        Research amberResearch = new Research(new NamespacedKey(this, "amber"), 6791, "琥珀", 20);
        amberResearch.addItems(treeScraper, amber, amberBlock);
        amberResearch.register();

        Research magicalMirrorResearch = new Research(new NamespacedKey(this, "magical_mirror"), 6792, "魔镜", 30);
        magicalMirrorResearch.addItems(blueEnderPearl, magicalMirror);
        magicalMirrorResearch.register();
        
        new MagicalMirrorListener(this, mirror);
	}

	private String[] getLore(String item, int chance) {
		return new String[] {
				"", 
				"&7几率: &a" + chance + "%",
				"&e右键点击任何原木&7以获取: " + item
		};
	}

	private void clearAttributes(SlimefunItemStack... items) {
		for (SlimefunItemStack item : items) {
			ItemMeta meta = item.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			item.setItemMeta(meta);
		}
	}

	@Override
	public String getBugTrackerURL() {
		return "https://github.com/ybw0014/SlimyTreeTaps-CN/issues";
	}

	@Override
	public JavaPlugin getJavaPlugin() {
		return this;
	}

}
