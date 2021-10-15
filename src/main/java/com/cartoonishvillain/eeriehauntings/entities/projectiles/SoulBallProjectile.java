package com.cartoonishvillain.eeriehauntings.entities.projectiles;

import com.cartoonishvillain.eeriehauntings.EerieHauntings;
import com.cartoonishvillain.eeriehauntings.Register;
import com.cartoonishvillain.eeriehauntings.capabilities.playercapability.PlayerCapability;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class SoulBallProjectile extends ProjectileItemEntity {
    public SoulBallProjectile(EntityType<? extends ProjectileItemEntity> p_i50157_1_, LivingEntity p_i50157_2_, World p_i50157_3_) {
        super(p_i50157_1_, p_i50157_2_, p_i50157_3_);
    }

    public SoulBallProjectile(EntityType<SoulBallProjectile> soulBallProjectileEntityType, World world) {
        super(soulBallProjectileEntityType, world);
    }

    @Override
    public ItemStack getItem(){return new ItemStack(Register.SOULBALL.get());}

    @Override
    protected Item getDefaultItem() {
        return Register.SOULBALL.get();
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
        super.onHitEntity(p_213868_1_);
        if (p_213868_1_.getEntity() instanceof PlayerEntity && !p_213868_1_.getEntity().level.isClientSide){
            PlayerEntity player = (PlayerEntity) p_213868_1_.getEntity();
            player.getCapability(PlayerCapability.INSTANCE).ifPresent(h->{
                h.addHauntChance(EerieHauntings.serverConfig.SOULBALLCHANCEADD.get().floatValue());
            });
        } if(p_213868_1_.getEntity() instanceof LivingEntity && !p_213868_1_.getEntity().level.isClientSide){
            ((LivingEntity) p_213868_1_.getEntity()).addEffect(new EffectInstance(Effects.WEAKNESS, 30*20, 0));
            p_213868_1_.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0);
        }
    }

    @Override
    protected void onHit(RayTraceResult p_70227_1_) {
        super.onHit(p_70227_1_);
        this.remove();
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
