package me.emafire003.dev.coloredglowlib.mixin;

import me.emafire003.dev.coloredglowlib.networking.PlayerJoinEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerJoinMixin {
    @Inject(at = @At(value = "TAIL"), method = "onPlayerConnect", cancellable = true)
    private void injectPlayerJoin(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        ActionResult result1 = PlayerJoinEvent.EVENT.invoker().joinServer(player, player.getServer());
        if (result1 == ActionResult.FAIL) {
            ci.cancel();
        }
    }
}
