package net.exenberger.mystore.service;

import org.springframework.hateoas.server.core.Relation;

@Relation(itemRelation = "store", collectionRelation = "stores")
public record SurroundingStoreDTO(StoreDTO store, double distance) {
}
