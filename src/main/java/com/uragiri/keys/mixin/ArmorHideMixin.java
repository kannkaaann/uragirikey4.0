package com.uragiri.keys.mixin;

import com.uragiri.keys.Impersonation;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorHideMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true, require = 0)
    private void uragiri$hide(CallbackInfo ci) {
        if (Impersonation.isGag()) {
            ci.cancel();
        }
    }
}
