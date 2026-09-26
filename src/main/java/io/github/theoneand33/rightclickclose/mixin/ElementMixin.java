package io.github.theoneand33.rightclickclose.mixin;

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
        if (button.button() != 1 || action != 1) {
            return;
        }
        try {
            Minecraft client = Minecraft.getInstance();
            Object screen = getCurrentScreen(client);
            if (screen == null) {
                return;
            }
            if (!shouldClose(screen) || isHoveringItemOrCarrying(screen)) {
                return;
            }
            screen.getClass().getMethod("onClose").invoke(screen);
            ci.cancel();
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            // ponytail: fail-closed, a failed lookup must never crash the game thread
        }
    }

    private static boolean isHoveringItemOrCarrying(Object screen) {
        try {
            Object menu = screen.getClass().getMethod("getMenu").invoke(screen);
            if (menu != null) {
                Object carried = menu.getClass().getMethod("getCarried").invoke(menu);
                if (carried != null && !(boolean) carried.getClass().getMethod("isEmpty").invoke(carried)) {
                    return true;
                }
            }
        } catch (ReflectiveOperationException | RuntimeException ignored) {
        }
        try {
            Object slot = getSlotField(screen, "hoveredSlot");
            if (slot != null && (boolean) slot.getClass().getMethod("hasItem").invoke(slot)) {
                return true;
            }
        } catch (ReflectiveOperationException | RuntimeException ignored) {
        }
        return false;
    }

    private static boolean shouldClose(Object screen) {
        try {
            Object result = screen.getClass().getMethod("shouldCloseOnEsc").invoke(screen);
            return !(result instanceof Boolean) || (boolean) result;
        } catch (ReflectiveOperationException | RuntimeException e) {
            return true;
        }
    }

    // ponytail: three lookups kept, screen moved from Minecraft (26.1) to Gui (26.2+)
    private static Object getCurrentScreen(Minecraft client) {
        if (client == null) {
            return null;
        }
        try {
            return client.getClass().getField("screen").get(client);
        } catch (ReflectiveOperationException | RuntimeException ignored) {
        }
        Object gui;
        try {
            gui = client.getClass().getField("gui").get(client);
        } catch (ReflectiveOperationException | RuntimeException e) {
            return null;
        }
        if (gui == null) {
            return null;
        }
        try {
            return gui.getClass().getMethod("screen").invoke(gui);
        } catch (NoSuchMethodException ignored) {
        } catch (ReflectiveOperationException | RuntimeException e) {
            return null;
        }
        Class<?> type = gui.getClass();
        while (type != null) {
            try {
                java.lang.reflect.Field field = type.getDeclaredField("screen");
                field.setAccessible(true);
                return field.get(gui);
            } catch (NoSuchFieldException e) {
                type = type.getSuperclass();
            } catch (ReflectiveOperationException | RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    private static Object getSlotField(Object screen, String name) {
        Class<?> type = screen.getClass();
        while (type != null) {
            try {
                java.lang.reflect.Field field = type.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(screen);
            } catch (NoSuchFieldException e) {
                type = type.getSuperclass();
            } catch (ReflectiveOperationException | RuntimeException e) {
                return null;
            }
        }
        return null;
    }
}
