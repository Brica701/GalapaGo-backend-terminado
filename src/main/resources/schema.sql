create table servicios
(
    id                        bigint auto_increment
        primary key,
    capacidad_max_persona     int                  null,
    cupo_disponible           int                  null,
    destacado                 tinyint(1)           null,
    habitaciones_disponibles  int                  null,
    precio_base_noche_persona double               null,
    categoria                 varchar(255)         null,
    descripcion               text                 null,
    imagen_url                text                 null,
    nombre                    varchar(255)         null,
    ubicacion                 varchar(255)         null,
    tiene_wifi                tinyint(1) default 0 null,
    tiene_desayuno            tinyint(1) default 0 null,
    tiene_transporte          tinyint(1) default 0 null,
    tiene_equipo              tinyint(1) default 0 null
);

create table detalle_servicio
(
    id            bigint auto_increment
        primary key,
    detalle_texto varchar(255) null,
    servicio_id   bigint       null,
    constraint fk_detalle_servicio
        foreign key (servicio_id) references servicios (id)
            on delete cascade
);

create table habitaciones
(
    id               bigint auto_increment
        primary key,
    servicio_id      bigint       not null,
    tipo             varchar(255) not null,
    precio_por_noche double       not null,
    cantidad_total   int          not null,
    constraint `1`
        foreign key (servicio_id) references servicios (id)
            on delete cascade
);

create index servicio_id
    on habitaciones (servicio_id);

create table servicio_detalles
(
    id            bigint auto_increment
        primary key,
    detalle_texto text   null,
    servicio_id   bigint null,
    constraint FK3khje1vmai8eyeopaxc61nx2d
        foreign key (servicio_id) references servicios (id)
);

create table usuarios
(
    id        bigint auto_increment
        primary key,
    apellidos varchar(255) null,
    email     varchar(255) not null,
    nombre    varchar(255) null,
    password  varchar(255) not null,
    rol       varchar(255) null,
    constraint email
        unique (email)
);

create table comentarios
(
    id                bigint auto_increment
        primary key,
    puntuacion        int                                       null,
    fecha_publicacion timestamp(6) default current_timestamp(6) null,
    servicio_id       bigint                                    null,
    usuario_id        bigint                                    null,
    texto             varchar(255)                              null,
    constraint fk_comentario_servicio
        foreign key (servicio_id) references servicios (id),
    constraint fk_comentario_usuario
        foreign key (usuario_id) references usuarios (id)
);

create table reservas
(
    id                    bigint auto_increment
        primary key,
    cantidad_habitaciones int          null,
    cantidad_personas     int          null,
    fecha_fin             date         null,
    fecha_inicio          date         null,
    precio_total          double       null,
    servicio_id           bigint       null,
    usuario_id            bigint       null,
    estado                varchar(255) null,
    tipo_habitacion       varchar(255) null,
    habitacion_id         bigint       null,
    constraint FKj1dyoxal4rnhdcxo4mv6bcivc
        foreign key (habitacion_id) references habitaciones (id),
    constraint fk_reserva_servicio
        foreign key (servicio_id) references servicios (id),
    constraint fk_reserva_usuario
        foreign key (usuario_id) references usuarios (id)
);

