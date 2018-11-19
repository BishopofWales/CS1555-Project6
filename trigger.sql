CREATE OR REPLACE TRIGGER trig_bidTimeUpdate
    before insert on Bidlog
    begin
    update ourSysDate   set c_date = c_date + 1/24/60/60*5;
    end ;
CREATE OR REPLACE TRIGGER trig_updateHighBid
    after insert on Bidlog
    begin
    null;
    /*To do: find bid that was just add (sort by date) and update the value of the product that corresponds to the bid*/
    end;
create or replace procedure proc_putProduct(name in varchar2,description in varchar2, category in varchar2,number_of_days in int) as
begin
    /*TO DO: insert into products tables, auction table, belongs to table*/
    null;
end proc_putProduct;
/*
select *, Max(bid_time) 
    from Bidlog 
    group by auction_id;
select *, Max(amount)
    from Bidlog
    group by auction_id;
*/