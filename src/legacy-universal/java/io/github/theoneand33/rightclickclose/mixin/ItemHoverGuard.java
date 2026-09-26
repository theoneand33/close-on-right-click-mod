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
        // ponytail: Yarn names it focusedSlot, try Mojmap hoveredSlot too if mappings drift
        for (String name : new String[] {"focusedSlot", "hoveredSlot"}) {
            Object slot = getSlotField(screen, name);
            if (slot instanceof Slot slotTyped && slotTyped.hasStack()) {
                return true;
            }
        }
        return false;
    }

    private static Object getSlotField(Screen screen, String name) {
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
