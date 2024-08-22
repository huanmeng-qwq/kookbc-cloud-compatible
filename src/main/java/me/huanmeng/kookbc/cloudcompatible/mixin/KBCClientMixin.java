package me.huanmeng.kookbc.cloudcompatible.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snw.jkook.command.CommandManager;
import snw.jkook.config.ConfigurationSection;
import snw.kookbc.impl.CoreImpl;
import snw.kookbc.impl.KBCClient;
import snw.kookbc.impl.command.CommandManagerImpl;
import snw.kookbc.impl.command.cloud.CloudCommandManagerImpl;
import snw.kookbc.impl.command.internal.CloudHelpCommand;
import snw.kookbc.impl.entity.builder.EntityBuilder;
import snw.kookbc.impl.entity.builder.MessageBuilder;
import snw.kookbc.impl.event.EventFactory;
import snw.kookbc.impl.event.internal.UserClickButtonListener;
import snw.kookbc.impl.network.NetworkClient;
import snw.kookbc.impl.plugin.InternalPlugin;
import snw.kookbc.impl.storage.EntityStorage;
import snw.kookbc.interfaces.network.NetworkSystem;
import snw.kookbc.util.ReturnNotNullFunction;

import java.io.File;

@Mixin(value = KBCClient.class, remap = false)
public class KBCClientMixin {
    @Mutable
    @Shadow
    @Final
    private CommandManager commandManager;

    @Shadow
    @Final
    private CoreImpl core;

    @Shadow
    @Final
    private InternalPlugin internalPlugin;

    @Inject(method = "<init>(Lsnw/kookbc/impl/CoreImpl;Lsnw/jkook/config/ConfigurationSection;Ljava/io/File;Ljava/lang/String;Lsnw/kookbc/util/ReturnNotNullFunction;Lsnw/kookbc/util/ReturnNotNullFunction;Lsnw/kookbc/util/ReturnNotNullFunction;Lsnw/kookbc/util/ReturnNotNullFunction;Lsnw/kookbc/util/ReturnNotNullFunction;Lsnw/kookbc/util/ReturnNotNullFunction;Lsnw/kookbc/util/ReturnNotNullFunction;)V",
            at = @At("TAIL"),
            remap = false
    )
    public void init(CoreImpl core, ConfigurationSection config, File pluginsFolder, String token,
                     @Nullable ReturnNotNullFunction<KBCClient, CommandManager> commandManager,
                     @Nullable ReturnNotNullFunction<KBCClient, NetworkClient> networkClient,
                     @Nullable ReturnNotNullFunction<KBCClient, EntityStorage> storage,
                     @Nullable ReturnNotNullFunction<KBCClient, EntityBuilder> entityBuilder,
                     @Nullable ReturnNotNullFunction<KBCClient, MessageBuilder> msgBuilder,
                     @Nullable ReturnNotNullFunction<KBCClient, EventFactory> eventFactory,
                     @Nullable ReturnNotNullFunction<KBCClient, NetworkSystem> networkSystem,
                     CallbackInfo ci) {
        if (this.commandManager.getClass().equals(CommandManagerImpl.class)) {
            KBCClient self = (KBCClient) (Object) this;
            //noinspection UnreachableCode
            this.commandManager = new CloudCommandManagerImpl(self);
        }
    }

    @SuppressWarnings("UnreachableCode")
    @Inject(method = "registerHelpCommand", at = @At("HEAD"), remap = false, cancellable = true)
    public void regHelp(CallbackInfo ci) {
        if (commandManager instanceof CloudCommandManagerImpl) {
            ci.cancel();
            KBCClient self = (KBCClient) (Object) this;
            ((CloudCommandManagerImpl) commandManager)
                    .registerCloudCommand(internalPlugin, new CloudHelpCommand(self));
            this.core.getEventManager()
                    .registerHandlers(this.internalPlugin, new UserClickButtonListener(self));
        }
    }
}
