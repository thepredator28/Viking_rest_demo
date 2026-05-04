package ru.mephi.vikingdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.controller.VikingListener;
import ru.mephi.vikingdemo.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class VikingService {
    private final Map<Integer, Viking> vikingSlovar = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);
    private final VikingFactory vikingFactory;
    @Autowired
    private VikingListener vikingListener;

    public VikingService(VikingFactory vikingFactory) {
        this.vikingFactory = vikingFactory;
    }

    public List<Viking> findAll() {
        return new ArrayList<>(vikingSlovar.values());
    }

    public Viking createRandomViking() {
        Viking randomViking = vikingFactory.createRandomViking();
        return addViking(randomViking);
    }

    public Viking addViking(Viking entity) {
        int id = idCounter.getAndIncrement();

        Viking newViking = new Viking(
                id,
                entity.name(),
                entity.age(),
                entity.heightCm(),
                entity.hairColor(),
                entity.beardStyle(),
                entity.equipment()
        );

        vikingSlovar.put(id, newViking);
        vikingListener.addViking(newViking);
        return newViking;
    }

    public void deleteViking(int id) {
        if (vikingSlovar.remove(id) != null) {
            vikingListener.deleteViking(id);
        }
    }

    public Viking updateViking(int id, VikingEntity updated) {
        Viking old = vikingSlovar.get(id);
        if (old == null){
            return null;
        }

        String name = (updated.name() != null) ? updated.name() : old.name();
        int age = (updated.age() != 0) ? updated.age() : old.age();
        int height = (updated.heightCm() != 0) ? updated.heightCm() : old.heightCm();
        HairColor hair = (updated.hairColor() != null) ? updated.hairColor() : old.hairColor();
        BeardStyle beard = (updated.beardStyle() != null) ? updated.beardStyle() : old.beardStyle();
        List<EquipmentItem> equipment = (updated.equipment() != null) ? updated.equipment() : old.equipment();

        Viking newViking = new Viking(id, name, age, height, hair, beard, equipment);

        vikingSlovar.put(id, newViking);
        vikingListener.updateViking(newViking);
        return newViking;
    }
}