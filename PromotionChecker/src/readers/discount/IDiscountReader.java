/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package readers.discount;

import db.entities.Discounts;

/**
 *
 * @author Miro
 */
public interface IDiscountReader {
    Discounts getNext();

    public boolean hasNext();
}
