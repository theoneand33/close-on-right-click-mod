package io.github.theoneand33.rightclickclose.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.Element;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Element.class)
public interface ModernElementMixin {
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    default void rightClickClosesMenu(Click click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (click.button() == 1 && client.currentScreen != null) {
            if (ItemHoverGuard.isHoveringItemOrCarrying(client.currentScreen)) {
                return;
            }
            client.currentScreen.close();
            cir.setReturnValue(true);
        }
    }
}
