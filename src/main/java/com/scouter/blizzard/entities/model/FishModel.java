package com.scouter.blizzard.entities.model;


import com.scouter.blizzard.entities.FishEntity;
import com.scouter.blizzard.entities.ModelLocations;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class FishModel<T extends FishEntity & GeoAnimatable> extends GeoModel<T>
{
    private ResourceLocation model;
    private ResourceLocation texture;
    private ResourceLocation animation;

    public FishModel(ModelLocations.ModelData modelData){
        this.model = modelData.getModel();
        this.texture = modelData.getTexture();
        this.animation = modelData.getAnimation();
    }

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return animation;
    }
}

