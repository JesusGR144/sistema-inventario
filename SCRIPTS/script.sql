/* 1.1) Describe el funcionamiento general de la sentencia JOIN.
JOIN nos ayuda a mezclar o combinar filas de dos o mas tablas dependiendo 
de la relación que existe entre las columnas de cada tabla

1.2) ¿Cuáles son los tipos de JOIN y cuál es el funcionamiento de los mismos?
INER JOIN: Nos devuelve solamente las filas que coinciden entre las tablas consultadas.

LEFT JOIN: Nos devuelve toda la información de la tabla izquierda incluyendo las coincidencias de la tabla derecha.

RIGHT JOIN: Nos devuelve toda la información de la tabla derecha incluyendo las coincidencias de la tabla izquierda.

FULL JOIN: Nos devuelve toda la información de las tablas consultadas, en otras palabras nos devuelve una sola tabla combinando su información.

SELF JOIN: Nos devuelve las relaciones que existen dentro de la misma tabla consultada.

1.3) ¿Cuál es el funcionamiento general de los TRIGGER y qué propósito tienen?
Los TRIGGER son acciones que se ejecutan automáticamente cuando modificas datos en la base de datos. 
Su propósito es automatizar tareas para que no tengas que hacerlas manualmente, como actualizar inventarios 
cuando registras una venta.

1.4) ¿Qué es y para qué sirve un STORED PROCEDURE?
Es similar a una función en programación que automatiza tareas que siempre se hacen igual, para poder reutilizarla
las veces necesarias.
*/

-- 1.5) Productos que tengan una venta:
SELECT DISTINCT p.*
FROM productos p
INNER JOIN ventas v ON p.idProducto = v.idProducto;

-- 1.6) Productos con ventas y cantidad total vendida:
SELECT p.*, SUM(v.cantidad) as total_vendido
FROM productos p
INNER JOIN ventas v ON p.idProducto = v.idProducto
GROUP BY p.idProducto, p.nombre, p.precio;

-- 1.7) Todos los productos y suma total vendida (incluso sin ventas):
SELECT p.*, 
       COALESCE(SUM(v.cantidad * p.precio), 0) as suma_total_vendida
FROM productos p
LEFT JOIN ventas v ON p.idProducto = v.idProducto
GROUP BY p.idProducto, p.nombre, p.precio;