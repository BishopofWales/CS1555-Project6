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

create or replace trigger trig_defaultSellDate
before insert on Product
for each row
begin 
	if ((:new.status = 'under auction') and (:new.sell_date is null)) then :new.sell_date := (:new.start_date + :new.number_of_days); end if;
end;
/

create or replace trigger trig_closeAuctions
before update on ourSysDATE
for each row 
begin
    
    update Product set status = 'closed' where (start_date + number_of_days) < :new.c_date AND status = 'under auction';
    
end;
/

CREATE OR REPLACE TRIGGER trig_bidTimeUpdate
    after insert on Bidlog
    begin
    update ourSysDate   set c_date = c_date + 1/24/60/60*5;
    end ;
/

CREATE OR REPLACE TRIGGER trig_updateHighBid
    before insert on Bidlog
    for each row
    declare
        bidID int;
        oldAmount int;
        currentTime date;
    begin
        select amount into oldAmount from Product where auction_id = :new.auction_id;
        select max(bidsn) + 1 into bidId from bidlog;
        select c_date into currentTime from ourSysDate;
        if oldAmount >= :new.amount then raise_application_error(-20000, 'Current bid higher than new bid'); 
        else update Product set amount = :new.amount, buyer = :new.bidder where auction_id = :new.auction_id;
             :new.bidsn := bidId;
             :new.bid_time := currentTime;
        end if;
    end;
/
/*
CREATE OR REPLACE TRIGGER trig_updateHighBid
    after update on Product
    for each row
    declare
        currentTime date;
        bidId int;
    begin
        select c_date into currentTime from ourSysDate;
        select max(bidsn) + 1 into bidId from Bidlog;
    
        if (((:old.amount >= :new.amount and :old.buyer <> :new.buyer) or (:old.amount > :new.amount and :old.buyer = :new.buyer)) and :old.auction_id = :new.auction_id) 
        then raise_application_error(-20000, 'Current bid higher than new bid'); 
        else insert into Bidlog values (BidId, :new.auction_id, :new.buyer, currentTime, :new.amount);
        end if;
    end;
/
*/
create or replace procedure proc_putProduct(name in varchar2,description in varchar2, user in varchar2, category in varchar2,number_of_days in int,minprice in int) as
v_ID int;
v_date date;
begin
    select c_date into v_date from ourSysDate;
    select max(auction_id) into v_ID
    from Product;
    
    insert into Product values(v_ID+1, name, description, user, v_date, minprice, number_of_days, 'under auction', null, null, null);
    insert into belongsto values (v_ID+1,category);
end proc_putProduct;
/

commit;