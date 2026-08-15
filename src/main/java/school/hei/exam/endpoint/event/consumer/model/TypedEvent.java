package school.hei.exam.endpoint.event.consumer.model;

import school.hei.exam.PojaGenerated;
import school.hei.exam.endpoint.event.model.PojaEvent;

@PojaGenerated
public record TypedEvent(String typeName, PojaEvent payload) {}
