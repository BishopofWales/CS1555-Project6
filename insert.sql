insert into Customer values ('bad68', 'hunter2', 'brandon', 'cato', 'bad68@pitt.edu');
insert into Customer values ('tom', 'hunter2', 'thomas', 'juliet', 'bad67@pitt.edu');
insert into Customer values ('tonyboy', 'hunter2', 'tony', 'ward', 'bad66@pitt.edu');

insert into Administrator values ('admin', 'root', 'brandon', 'cato', 'brandondon@gmail.com');

insert into Product values (1, 'computer', 'not good', 'bad68', sysdate-365, 100, 1, 'under auction', 'tom', null, 120);
insert into Product values (2, 'computer', 'pretty good', 'bad68', sysdate-6, 150, 10, 'under auction', 'tom', null, 150);
insert into Product values (3, 'mobile phone', 'broken screen', 'tom', sysdate-270, 80, 30, 'under auction', 'tonyboy', null, 80);
insert into Product values (4, 'guitar', 'great shape', 'tonyboy', sysdate-30, 400, 20, 'under auction', null, null, null);
insert into Product values (5, 'guitar case', 'keeps guitar safe', 'tonyboy', sysdate-30, 100, 20, 'sold', 'tom', sysdate, 100);

insert into Bidlog values (1, 1, 'tom', sysdate, 120);
insert into Bidlog values(11,5,'tom',sysdate,100);
insert into Bidlog values (2, 2, 'tom', sysdate, 150);
insert into Bidlog values (3, 2, 'tonyboy', sysdate-5, 125);
insert into Bidlog values (4, 1, 'tom', sysdate-10, 100);
insert into Bidlog values (5, 1, 'tonyboy', sysdate-7, 110);
insert into Bidlog values (6, 3, 'bad68', sysdate-15, 70);
insert into Bidlog values (7, 3, 'tonyboy', sysdate-5, 80);
insert into Bidlog values (8, 1, 'tonyboy', sysdate-10, 110);
insert into Bidlog values (9, 1, 'tom', sysdate-8, 115);
insert into Bidlog values (10,2, 'tom', sysdate-3, 130);

insert into Category values ('Electronics', null);
insert into Category values ('Computers', 'Electronics');
insert into Category values ('Phones', 'Electronics');
insert into Category values ('Musical Instruments', null);
insert into Category values ('Guitars', 'Musical Instruments');

insert into BelongsTo values (1, 'Computers');
insert into BelongsTo values (2, 'Computers');
insert into BelongsTo values (3, 'Phones');
insert into BelongsTo values (4, 'Guitars');
insert into BelongsTo values (5, 'Guitars');

insert into ourSysDATE values (sysdate);
commit;

select * from Product;
select * from ourSysDATE;

update ourSysDATE set c_date = sysdate + 5;
commit; 

select * from Product;
select * from ourSysDATE;

select func_bidCount(12, 'tom') from Bidlog;

select func_productCount(12, 'Guitars') from Product;

select func_buyingAmount(12, 'tom') from Product;

select * from product where REGEXP_LIKE(description,'.*good.*') and REGEXP_LIKE(description,'.*pretty.*')