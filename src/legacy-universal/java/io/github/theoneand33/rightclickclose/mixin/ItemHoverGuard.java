package io.github.theoneand33.rightclickclose.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;

public final class ItemHoverGuard {
    private ItemHoverGuard() {
    }

    public static boolean isHoveringItemOrCarrying(Screen screen) {
        if (screen instanceof HandledScreen<?> handled
                && handled.getScreenHandler() != null
                && !handled.getScreenHandler().getCursorStack().isEmpty()) {
            return true;
        }
        return hoveredSlotHasStack(screen);
    }

    private static boolean hoveredSlotHasStack(Screen screen) {
        Class<?> type = screen.getClass();
        while (type != null) {
            try {
                java.lang.reflect.Field field = type.getDeclaredField("focusedSlot");
                field.setAccessible(true);
                Object slot = field.get(screen);
                return slot instanceof Slot slotTyped && slotTyped.hasStack();
            } catch (NoSuchFieldException e) {
                type = type.getSuperclass();
            } catch (ReflectiveOperationException e) {
                return false;
            }
        }
        return false;
    }
}
