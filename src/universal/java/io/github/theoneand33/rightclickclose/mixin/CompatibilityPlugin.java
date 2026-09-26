package io.github.theoneand33.rightclickclose.mixin;

import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class CompatibilityPlugin implements IMixinConfigPlugin {
    private final String minecraftVersion;

    public CompatibilityPlugin() {
        minecraftVersion = FabricLoader.getInstance().getModContainer("minecraft")
                .map(container -> container.getMetadata().getVersion().getFriendlyString())
                .orElse("");
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.endsWith("LegacyElementMixin")) {
            return versionSupportsLegacy();
        }
        if (mixinClassName.endsWith("ModernElementMixin")) {
            return versionSupportsModern();
        }
        return true;
    }

    private boolean versionSupportsLegacy() {
        if (minecraftVersion.equals("1.21")) {
            return true;
        }
        Integer minor = minorAfter121();
        return minor != null && minor < 9;
    }

    private boolean versionSupportsModern() {
        Integer minor = minorAfter121();
        return minor != null && minor >= 9;
    }

    // ponytail: leading-digits parse, suffixes like +build.6/-rc1 must not crash mixin loading
    private Integer minorAfter121() {
        if (!minecraftVersion.startsWith("1.21.")) {
            return null;
        }
        String rest = minecraftVersion.substring(5);
        int end = 0;
        while (end < rest.length() && Character.isDigit(rest.charAt(end))) {
            end++;
        }
        if (end == 0) {
            return null;
        }
        try {
            return Integer.parseInt(rest.substring(0, end));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String mixinPackage, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }
}
