package com.wiatec.blive.pay

/**
 * Created by patrick on 01/11/2017.
 * create time : 9:58 AM
 */
class PayResultInfo {

    private var id: Int = 0
    private var channelName: String? = null
    private var auth: String? = null
    private var payId: String? = null
    private var state: String? = null
    private var cart: String? = null
    private var paymentMethod: String? = null
    private var paymentStatus: String? = null
    private var email: String? = null
    private var firstName: String? = null
    private var lastName: String? = null
    private var payerId: String? = null
    private var phone: String? = null
    private var countryCode: String? = null
    private var price: Float = 0.toFloat()
    private var currency: String? = null
    private var description: String? = null
    private var transactionFee: Float = 0.toFloat()
    private var createTime: String? = null
    private var updateTime: String? = null
    private var time: String? = null

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getChannelName(): String? {
        return channelName
    }

    fun setChannelName(channelName: String) {
        this.channelName = channelName
    }

    fun getAuth(): String? {
        return auth
    }

    fun setAuth(auth: String) {
        this.auth = auth
    }

    fun getPayId(): String? {
        return payId
    }

    fun setPayId(payId: String) {
        this.payId = payId
    }

    fun getState(): String? {
        return state
    }

    fun setState(state: String) {
        this.state = state
    }

    fun getCart(): String? {
        return cart
    }

    fun setCart(cart: String) {
        this.cart = cart
    }

    fun getPaymentMethod(): String? {
        return paymentMethod
    }

    fun setPaymentMethod(paymentMethod: String) {
        this.paymentMethod = paymentMethod
    }

    fun getPaymentStatus(): String? {
        return paymentStatus
    }

    fun setPaymentStatus(paymentStatus: String) {
        this.paymentStatus = paymentStatus
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getFirstName(): String? {
        return firstName
    }

    fun setFirstName(firstName: String) {
        this.firstName = firstName
    }

    fun getLastName(): String? {
        return lastName
    }

    fun setLastName(lastName: String) {
        this.lastName = lastName
    }

    fun getPayerId(): String? {
        return payerId
    }

    fun setPayerId(payerId: String) {
        this.payerId = payerId
    }

    fun getPhone(): String? {
        return phone
    }

    fun setPhone(phone: String) {
        this.phone = phone
    }

    fun getCountryCode(): String? {
        return countryCode
    }

    fun setCountryCode(countryCode: String) {
        this.countryCode = countryCode
    }

    fun getPrice(): Float {
        return price
    }

    fun setPrice(price: Float) {
        this.price = price
    }

    fun getCurrency(): String? {
        return currency
    }

    fun setCurrency(currency: String) {
        this.currency = currency
    }

    fun getDescription(): String? {
        return description
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun getTransactionFee(): Float {
        return transactionFee
    }

    fun setTransactionFee(transactionFee: Float) {
        this.transactionFee = transactionFee
    }

    fun getCreateTime(): String? {
        return createTime
    }

    fun setCreateTime(createTime: String) {
        this.createTime = createTime
    }

    fun getUpdateTime(): String? {
        return updateTime
    }

    fun setUpdateTime(updateTime: String) {
        this.updateTime = updateTime
    }

    fun getTime(): String? {
        return time
    }

    fun setTime(time: String) {
        this.time = time
    }

    override fun toString(): String {
        return "PayResultInfo{" +
                "id=" + id +
                ", channelName='" + channelName + '\'' +
                ", auth='" + auth + '\'' +
                ", payId='" + payId + '\'' +
                ", state='" + state + '\'' +
                ", cart='" + cart + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", payerId='" + payerId + '\'' +
                ", phone='" + phone + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", description='" + description + '\'' +
                ", transactionFee=" + transactionFee +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", time='" + time + '\'' +
                '}'
    }
}