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