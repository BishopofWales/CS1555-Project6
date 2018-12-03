drop table ourSysDATE cascade constraints;
drop table Customer cascade constraints;
drop table Administrator cascade constraints;
drop table Product cascade constraints;
drop table Bidlog cascade constraints;
drop table Category cascade constraints;
drop table BelongsTo cascade constraints;

create table ourSysDATE (
    c_date date,
    constraint ourSysDATE_PK primary key (c_date)
);

create table Customer (
    login varchar2(10),
    password varchar2(10),
    name varchar2(20),
    address varchar2(30),
    email varchar2(20),
    constraint Customer_PK primary key (login)
);

create table Administrator (
    login varchar2(10),
    password varchar2(10),
    name varchar2(10),
    address varchar2(30),
    email varchar2(20),
    constraint Administrator_PK primary key (login)
);

create table Product (
    auction_id int,
    name varchar2(20),
    description varchar2(30),
    seller varchar2(10),
    start_date date,
    min_price int,
    number_of_days int,
    status varchar2(15) not null,
    buyer varchar2(10),
    sell_date date,
    amount int,
    constraint Product_PK primary key (auction_id),
    constraint Product_Customer_Buy_FK foreign key (seller) references Customer,
    constraint Product_Customer_Sell_FK foreign key (buyer) references Customer
);

create table Bidlog (
    bidsn int,
    auction_id int,
    bidder varchar2(10),
    bid_time date,
    amount int,
    constraint Bidlog_PK primary key (bidsn),
    constraint Bidlog_Product_FK foreign key (auction_id) references Product,
    constraint Bidlog_Customer_FK foreign key (bidder) references Customer
    /*To do, check that the newest added bid is also the largest for its product */
);

create table Category (
    name varchar2(20),
    parent_category varchar2(20),
    constraint Category_PK primary key (name),
    constraint Category_Parent foreign key (parent_category) references Category(name)
);

create table BelongsTo (
    auction_id int,
    category varchar2(20),
    constraint BelongsTo_PK primary key (auction_id, category),
    constraint BelongsTo_Product_FK foreign key (auction_id) references Product,
    constraint BelongsTo_Category_FK foreign key (category) references Category(name)
);

commit;


select * from product where REGEXP_LIKE(description,'.*good.*') and REGEXP_LIKE(description,'.*pretty.*');