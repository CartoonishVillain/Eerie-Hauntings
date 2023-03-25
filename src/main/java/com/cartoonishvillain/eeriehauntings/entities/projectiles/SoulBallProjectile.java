package com.cartoonishvillain.eeriehauntings.entities.projectiles;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import com.cartoonishvillain.eeriehauntings.Register;
import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public class SoulBallProjectile extends ThrowableItemProjectile {
    public SoulBallProjectile(EntityType<? extends ThrowableItemProjectile> p_i50157_1_, LivingEntity p_i50157_2_, Level p_i50157_3_) {
        super(p_i50157_1_, p_i50157_2_, p_i50157_3_);
    }

    public SoulBallProjectile(EntityType<SoulBallProjectile> soulBallProjectileEntityType, Level world) {
        super(soulBallProjectileEntityType, world);
    }

    @Override
    public ItemStack getItem(){return new ItemStack(Register.SOULBALL.get());}

    @Override
    protected Item getDefaultItem() {
        return Register.SOULBALL.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult p_213868_1_) {
        super.onHitEntity(p_213868_1_);
        if (p_213868_1_.getEntity() instanceof Player && !p_213868_1_.getEntity().level.isClientSide){
            Player player = (Player) p_213868_1_.getEntity();
            player.getCapability(PlayerCapability.INSTANCE).ifPresent(h-> h.addHauntChance(EerieHauntings.serverConfig.SOULBALLCHANCEADD.get().floatValue()));
        } if(p_213868_1_.getEntity() instanceof LivingEntity && !p_213868_1_.getEntity().level.isClientSide){
            ((LivingEntity) p_213868_1_.getEntity()).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 30*20, 0));
            p_213868_1_.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0);
        }
    }

    @Override
    protected void onHit(HitResult p_70227_1_) {
        super.onHit(p_70227_1_);
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
