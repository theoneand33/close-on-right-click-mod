package com.example.rightclickclose.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class ElementMixin {
    @Inject(method = "onButton", at = @At("HEAD"))
    private void rightClickClosesMenu(long window, MouseButtonInfo button, int action, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (button.button() == 1 && action == 1 && client.gui.screen() != null) {
            client.setScreenAndShow(null);
        }
    }
}
