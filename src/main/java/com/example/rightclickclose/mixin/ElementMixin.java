package com.example.rightclickclose.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiEventListener.class)
public interface ElementMixin {
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    default void rightClickClosesMenu(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
        if (event.button() == 1) {
            Minecraft.getInstance().setScreenAndShow(null);
            cir.setReturnValue(true);
        }
    }
}
