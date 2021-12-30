export interface IProduct {
  id?: number;
  name?: string | null;
  price?: number | null;
  category?: string | null;
  color?: string | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string | null,
    public price?: number | null,
    public category?: string | null,
    public color?: string | null
  ) {}
}
