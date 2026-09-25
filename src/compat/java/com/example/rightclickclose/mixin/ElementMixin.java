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
    @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
    private void rightClickClosesMenu(long window, MouseButtonInfo button, int action, CallbackInfo ci) {
        var screen = Minecraft.getInstance().screen;
        if (button.button() == 1 && action == 1 && screen != null) {
            screen.onClose();
            ci.cancel();
        }
    }
}
