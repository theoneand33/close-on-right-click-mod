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
        return minecraftVersion.equals("1.21") || minecraftVersion.startsWith("1.21.")
                && Integer.parseInt(minecraftVersion.substring(5).split("\\.")[0]) < 9;
    }

    private boolean versionSupportsModern() {
        return minecraftVersion.startsWith("1.21.") && Integer.parseInt(minecraftVersion.substring(5).split("\\.")[0]) >= 9;
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
