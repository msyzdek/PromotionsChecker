package readers.product;

import db.entities.Products;

/**
 *
 * @author Miro
 */
public interface IProductReader {
    Products getNext();

    public boolean hasNext();
}
