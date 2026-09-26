package com.example.rightclickclose.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Element.class)
public interface LegacyElementMixin {
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    default void rightClickClosesMenu(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (button == 1 && client.currentScreen != null) {
            if (ItemHoverGuard.isHoveringItemOrCarrying(client.currentScreen)) {
                return;
            }
            client.currentScreen.close();
            cir.setReturnValue(true);
        }
    }
}
