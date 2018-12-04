create or replace function func_productCount (x in number, c in varchar2) 
return number 
is prodCount number;
   pastDate date;
   currentDate date;
begin
select c_date into currentDate from ourSysDATE;
pastDate := add_months(currentDate, -1*x);
select count(*) into prodCount from Product p, BelongsTo b where p.auction_id = b.auction_id AND p.status = 'sold' AND p.sell_date >= pastDate AND b.category = c;
return prodCount;
end;
/

create or replace function func_bidCount (x in number, u in varchar2)
return number
is bidCount number;
   pastDate date;
   currentDate date;
begin
select c_date into currentDate from ourSysDATE;
pastDate := add_months(currentDate, -1*x);
select count(*) into bidCount from Bidlog b where b.bidder = u and bid_time >= pastDate;
return bidCount;
end;
/

create or replace function func_buyingAmount (x in number, u in varchar2)
return number
is buyAmount number;
   pastDate date;
   currentDate date;
begin
select c_date into currentDate from ourSysDATE;
pastDate := add_months(currentDate, -1*x);
select sum(amount) into buyAmount from product where status = 'sold' AND buyer = u and sell_date >= pastDate;
return buyAmount;
end;
/

create or replace trigger trig_closeAuctions
after update on ourSysDATE
for each row 
begin
    update Product set status = 'closed' where (start_date + number_of_days) < :new.c_date AND status = 'under auction';
end;
/
commit;
CREATE OR REPLACE TRIGGER trig_bidTimeUpdate
    before insert on Bidlog
    begin
    update ourSysDate   set c_date = c_date + 1/24/60/60*5;
    end ;
/
CREATE OR REPLACE TRIGGER trig_updateHighBid
    after insert on Bidlog
    begin
    null;
    /*To do: find bid that was just added (sort by date) and update the value of the product that corresponds to the bid*/
    end;
/
create or replace procedure proc_putProduct(name in varchar2) as /*name in varchar2,description in varchar2, user in varchar2, category in varchar2,number_of_days in int,minprice in int) as*/

begin
    /*
    select c_date into v_date from ourSysDate;
    select max(auction_id) into v_ID
    from Product;
    
    insert into Product (auction_id,name,description,seller,start_date,min_price,number_of_days,status,buyer,sell_date,amount) values(123,'name','desc.', null, null,0,200,200,'sold',null,100);*/
    /*, name, description, user, v_date, minprice, number_of_days, 'under auction', '', '', '');*/
    insert into BelongsTo values (5000, 'Guitars');

    
end proc_putProduct;
/
/*
call proc_putProduct('gaming PC');
insert into BelongsTo values (5000, 'Guitars');
select * from BelongsTo;*
*/
/*

select *, Max(bid_time) 
    from Bidlog 
    group by auction_id;
select *, Max(amount)
    from Bidlog
    group by auction_id;
*/
