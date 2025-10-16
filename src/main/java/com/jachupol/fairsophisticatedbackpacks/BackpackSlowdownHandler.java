package com.jachupol.expensiveshulkers;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.common.NeoForge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.registries.BuiltInRegistries;
import top.theillusivec4.curios.api.CuriosApi;


public class BackpackSlowdownHandler {

    // ID przedmiotów z Sophisticated Backpacks
    private static final ResourceLocation LEATHER    = ResourceLocation.parse("sophisticatedbackpacks:backpack");
    private static final ResourceLocation COPPER     = ResourceLocation.parse("sophisticatedbackpacks:copper_backpack");
    private static final ResourceLocation IRON       = ResourceLocation.parse("sophisticatedbackpacks:iron_backpack");
    private static final ResourceLocation GOLD       = ResourceLocation.parse("sophisticatedbackpacks:gold_backpack");
    private static final ResourceLocation DIAMOND    = ResourceLocation.parse("sophisticatedbackpacks:diamond_backpack");
    private static final ResourceLocation NETHERITE  = ResourceLocation.parse("sophisticatedbackpacks:netherite_backpack");

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        // tylko po stronie serwera
        if (player.level().isClientSide()) return;

        int backpacks = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.isEmpty()) continue;
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (id == null) continue;
            if (id.equals(COPPER) || id.equals(IRON) || id.equals(GOLD)
                    || id.equals(DIAMOND) || id.equals(NETHERITE) || id.equals(LEATHER)) {
                backpacks += stack.getCount();
            }
        }

        // więcej niż 1 plecak -> Slowness V (~75%); czas 40 ticków (odświeżane co tick)
        if (backpacks > 1) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 4, true, false, true));
        }
    }
}
