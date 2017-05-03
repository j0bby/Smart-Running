from django.contrib.auth.models import User
from rest_auth.serializers import UserDetailsSerializer
from rest_framework import serializers

from smart_running.models import Route, Marker, CompletedRoute


class RouteSerializer(serializers.ModelSerializer):
    publisher = 'smart_running.serializers.UserSerializer'

    class Meta:
        model = Route
        fields = ('id', 'date_published', 'last_updated', 'title', 'description',
                  'mode', 'difficulty', 'rating', 'markers')  # markers too
        read_only_fields = ('publisher', 'date_published', 'last_updated', 'rating')
        # TODO set read only fields on create()

    def validate_markers(self, value):
        if len(value) == 0:
            raise serializers.ValidationError("At least 1 marker must be specified")
        return value

    def create(self, validated_data):
        publisher_id = self.initial_data['publisher_id']
        publisher = User.objects.get(pk=publisher_id)

        route = Route.objects.create(
            publisher=publisher,
            title=validated_data['title'],
            description=validated_data['description'],
            mode=validated_data['mode'],
            difficulty=validated_data['difficulty']
            # markers=validated_data['markers']
        )

        for m in validated_data['markers']:
            m.routes.add(route)

        return route


class UserSerializer(UserDetailsSerializer):
    profile_type = serializers.CharField(source="userprofile.profile_type")
    birth_date = serializers.CharField(source="userprofile.birth_date")
    email_verified = serializers.BooleanField(source="userprofile.email_verified")
    total_distance = serializers.FloatField(source="userprofile.total_distance")
    total_duration = serializers.IntegerField(source="userprofile.total_duration")
    total_completed = serializers.IntegerField(source="userprofile.total_completed")

    class Meta(UserDetailsSerializer.Meta):
        fields = UserDetailsSerializer.Meta.fields + \
                 ('profile_type', 'birth_date', 'email_verified', 'total_distance', 'total_duration', 'total_completed')

    def update(self, instance, validated_data):
        profile_data = validated_data.pop('userprofile', {})

        profile_type = profile_data.get('profile_type')
        birth_date = profile_data.get('birth_date')
        email_verified = profile_data.get('email_verified')
        total_distance = profile_data.get('total_distance')
        total_duration = profile_data.get('total_duration')
        total_completed = profile_data.get('total_completed')

        instance = super(UserSerializer, self).update(instance, validated_data)

        profile = instance.userprofile
        if profile_data:
            if profile_type:
                profile.profile_type = profile_type
            if birth_date:
                profile.birth_date = birth_date
            if email_verified:
                profile.email_verified = email_verified
            if total_distance:
                profile.total_distance = total_distance
            if total_duration:
                profile.total_duration = total_duration
            if total_completed:
                profile.total_duration = total_completed

            profile.save()

        return instance


class MarkerSerializer(serializers.ModelSerializer):
    class Meta:
        model = Marker
        fields = ('id', 'title', 'description', 'full_description', 'latitude',
                  'longitude', 'zone_radius', 'clue', 'target_image', 'target_texture')


class CompletedRouteSerializer(serializers.ModelSerializer):
    class Meta:
        model = CompletedRoute
        fields = '__all__'
