package com.example.rightclickclose.mixin;

import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void rightClickClosesMenu(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (button == 1) {
            ((Screen) (Object) this).close();
            cir.setReturnValue(true);
        }
    }
}
