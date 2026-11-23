package com.uragiri.keys.mixin;

import com.uragiri.keys.Impersonation;
import java.util.UUID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class SkinMixin {
    private static final UUID GAG_UUID = new UUID(0L, 0L);

    @Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true, require = 0)
    private void uragiri$skin(CallbackInfoReturnable<SkinTextures> cir) {
        if (Impersonation.isGag()) {
            cir.setReturnValue(DefaultSkinHelper.getSkinTextures(GAG_UUID));
            return;
        }
        AbstractClientPlayerEntity self = (AbstractClientPlayerEntity) (Object) this;
        AbstractClientPlayerEntity target = Impersonation.findTarget(self);
        if (target == null) {
            return;
        }
        ClientPlayNetworkHandler handler = MinecraftClient.getInstance().getNetworkHandler();
        if (handler == null) {
            return;
        }
        PlayerListEntry entry = handler.getPlayerListEntry(target.getUuid());
        if (entry != null) {
            cir.setReturnValue(entry.getSkinTextures());
        }
    }
}
