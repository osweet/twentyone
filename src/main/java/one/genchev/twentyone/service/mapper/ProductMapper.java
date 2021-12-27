package one.genchev.twentyone.service.mapper;

import one.genchev.twentyone.domain.Product;
import one.genchev.twentyone.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {}
